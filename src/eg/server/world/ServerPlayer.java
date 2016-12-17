package eg.server.world;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

import eg.game.world.objects.player.Weapon;
import eg.server.Server;
import eg.server.world.ServerWorld.MsgType;

public class ServerPlayer 
{
	//private static final float MOVE_SPEED = 25f;
	
	private Socket socket;
	private int id;
	private DataOutputStream outToClient;
	private DatagramPacket udpPacket;
	private byte[] sendData;
	private DatagramSocket clientSocket;
	//private long lastUpdated;
	private int x, y, rot, weaponID, shootRot, health, killer;
	private volatile boolean dead;
	private Object lockObj;
	
	public ServerPlayer(Socket inSocket, int inID)
	{
		this.socket = inSocket;
		this.id = inID;
		this.sendData = new byte[Server.PACKET_SIZE];
		//this.lastUpdated = System.currentTimeMillis();
		this.x = 348;
		this.y = 348;
		this.rot = 0;
		this.weaponID = 0;
		this.health = 100;
		this.killer = -1;
		this.dead = false;
		this.lockObj = new Object();
		
		try 
		{
			outToClient = new DataOutputStream(socket.getOutputStream());
			udpPacket = new DatagramPacket(sendData, sendData.length, socket.getInetAddress(), socket.getPort()+1);
			clientSocket = new DatagramSocket();
			
			if (inID != -1)
				sendTCPMessage("CONNECTED|"+id);
			else
				sendTCPMessage("REJECTED");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * query functions
	 */
	
	public int getID()
	{
		return id;
	}
	
	public int getX() 
	{
		return x;
	}
	
	public int getY() 
	{
		return y;
	}
	
	public int getRot() 
	{
		return rot;
	}

	public int getShootRot() 
	{
		return shootRot;
	}
	
	public int getWeaponID()
	{
		return weaponID;
	}
	
	public int getHealth() 
	{
		return health;
	}
	
	/*
	 * net event functions
	 */
	
	private void updatePlayerPos(String substring) 
	{
		//parse the new position
		int inX = Integer.parseInt(substring.substring(0, substring.indexOf(',')));
		substring = substring.substring(substring.indexOf(',') + 1);
		int inY = Integer.parseInt(substring.substring(0, substring.indexOf(',')));
		substring = substring.substring(substring.indexOf(',') + 1);
		int inRot = Integer.parseInt(substring.substring(0, substring.indexOf(',')));
		
		//find the delta and update last updated time
		//float delta = (System.currentTimeMillis() - lastUpdated) / 100f;
		//lastUpdated = System.currentTimeMillis();
		
		//TODO EXPERIMENTAL anti-cheat
		//if (Math.abs(inX - x) / delta > MOVE_SPEED || Math.abs(inY - y) / delta > MOVE_SPEED)
		//	System.out.println("player " + id + " may be cheating");
		
		//update the positions
		x = inX;
		y = inY;
		rot = inRot;
		
		//tell everyone else about our new pos
		Server.getWorld().sendToAll(MsgType.UPDATE_POS, this, true);
	}
	
	private void shootBullet(String substring) 
	{
		shootRot = Integer.parseInt(substring.substring(0, substring.indexOf(',')));
		
		Server.getWorld().sendToAll(MsgType.SHOOT, this, true);
	}
	
	private void hitBullet(String substring) 
	{
		int bulletID = Integer.parseInt(substring.substring(0, substring.indexOf(',')));
		substring = substring.substring(substring.indexOf(',') + 1);
		int weaponID = Integer.parseInt(substring.substring(0, substring.indexOf(',')));
		int damage = Weapon.getFromID(weaponID).getDamage();
		
		health -= damage;
		if (health <= 0)
		{
			killer = bulletID;
			
			//wait until we are confirmed dead by client to respawn
			new Thread(new Runnable() 
			{
				@Override
				public void run() 
				{
					//TODO better anti cheat
					
					while (!dead)
					{
						try 
						{
							synchronized (lockObj)
							{
								lockObj.wait();
							}
						} catch (InterruptedException e) {}
					}
					
					respawn();
				}
			}).start();
		}
	}
	
	private void die()
	{
		dead = true;
		synchronized (lockObj)
		{
			lockObj.notify();
		}
	}
	
	private void respawn()
	{
		health = 100;
		dead = false;
		x = 100;
		y = 100;
		Server.getWorld().sendToAll(MsgType.SPAWN, this);
	}
	
	/*
	 * network functions
	 */
	
	void sendTCPMessage(String in) 
	{
		try 
		{
			outToClient.write((in+'\n').getBytes());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	void sendUDPMessage(String msg)
	{
		//send a message over udp
	    try 
	    {
	    	sendData = null;
	    	sendData = msg.getBytes();
	    	udpPacket.setData(sendData);
			clientSocket.send(udpPacket);
		} catch (IOException e) 
	    {
			e.printStackTrace();
		}
	}
	
	public void receiveTCPMessage(String in) 
	{
		try
		{
			if (in.startsWith("HIT"))
			{
				hitBullet(in.substring(in.indexOf('|') + 1));
			} else if (in.startsWith("DEAD"))
			{
				die();
			}
		} catch (Exception e)
		{
			System.out.println("TCP messsage from client " + id + " caused error.\n msg: " + in);
			//don't crash
		}
	}
	
	public void receiveUDPMessage(String in) 
	{
		try
		{
			if (in.startsWith("POS"))
			{
				updatePlayerPos(in.substring(in.indexOf('|') + 3));
			} else if (in.startsWith("SHOOT"))
			{
				shootBullet(in.substring(in.indexOf('|') + 3));
			}
		} catch (Exception e)
		{
			System.out.println("UDP messsage from client " + id + " caused error.\n msg: " + in);
			//don't crash
		}
	}
	
	public void close() 
	{
		try
		{
			outToClient.close();
			clientSocket.close();
			socket.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
