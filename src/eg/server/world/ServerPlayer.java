package eg.server.world;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

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
	private int x, y, rot, weaponID, shootRot;
	
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
	
	public int getID()
	{
		return id;
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
		// TODO Auto-generated method stub
		System.out.println(in);
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
			System.out.println("messsage from client " + id + " caused error.\n msg: " + in);
			//don't crash
		}
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
}
