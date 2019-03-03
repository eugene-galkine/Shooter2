package eg.server.world;

import eg.game.world.objects.player.Weapon;
import eg.server.net.Server;
import eg.server.net.TCPConnection;
import eg.server.net.UDPConnection;
import static eg.utils.GlobalConstants.*;
import static eg.utils.ByteArrayUtils.*;

public class ServerPlayer 
{
	//private static final float MOVE_SPEED = 25f;
	
	private final TCPConnection tcpConnection;
	private final UDPConnection udpConnection;
	private final int id;
	//private long lastUpdated;
	private int x;
	private int y;
	private int rot;
	private final int weaponID;
	private int health;
	private int killer;//TODO
	private float fx, fy, fRot;//TODO look into this
	private volatile boolean dead;
	private final Object lockObj;
	
	public ServerPlayer(TCPConnection tcpConnection, UDPConnection udpConnection, int inID)
	{
		this.tcpConnection = tcpConnection;
		this.udpConnection = udpConnection;
		this.id = inID;
		//this.lastUpdated = System.currentTimeMillis();
		this.x = 348;//TODO spawn
		this.y = 348;
		this.rot = 0;
		this.weaponID = 0;
		this.health = 100;
		this.killer = -1;
		this.dead = false;
		this.lockObj = new Object();
		
		if (inID != -1) {
			byte[] data = new byte[5];
			data[0] = TCP_CMD_CONNECTED;
			appendInt(data, 1, id);
			sendTCPMessage(data);
		} else {
			sendTCPMessage(new byte[]{TCP_CMD_REJECTED});
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

	public float getfX()
	{
		return fx;
	}
	
	public float getfY()
	{
		return fy;
	}
	
	public float getfRot() 
	{
		return fRot;
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
	
	private void updatePlayerPos(byte[] data, int index) 
	{
		//parse the new position
		int inX = parseInt(data, index);
		index += 4;
		int inY = parseInt(data, index);
		index += 4;
		int inRot = parseInt(data, index);
		
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
		Server.getWorld().sendToAll(UDP_CMD_POSITION, this, true);
	}
	
	private void shootBullet(byte[] data, int index) 
	{
		//shootRot = Integer.parseInt(substring.substring(0, substring.indexOf(',')));
		fx = parseFloat(data, index);
		index += 4;
		fy = parseFloat(data, index);
		index += 4;
		fRot = parseFloat(data, index);
		
		Server.getWorld().sendToAll(TCP_CMD_SHOOT, this, true);
	}
	
	private void throwGernade(byte[] data, int index)
	{
		fx = parseFloat(data, index);
		index += 4;
		fy = parseFloat(data, index);
		index += 4;
		fRot = parseFloat(data, index);
		
		Server.getWorld().sendToAll(TCP_CMD_GRENADE, this, false);
	}
	
	private void hitBullet(byte[] data, int index) 
	{
		int bulletID = parseInt(data, index);
		index += 4;
		int weaponID = parseInt(data, index);
		Weapon weapon = Weapon.getFromID(weaponID);
		int damage = 0;
		if (weapon != null)
			damage = weapon.getDamage();
		
		health -= damage;
		if (health <= 0)
		{
			killer = bulletID;
			
			//wait until we are confirmed dead by client to respawn
			new Thread(() -> {
				//TODO better anti cheat

				while (!dead)
				{
					try
					{
						synchronized (lockObj)
						{
							lockObj.wait();
						}
					} catch (InterruptedException ignore) {}
				}

				respawn();
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
	
	void respawn()
	{
		health = 100;
		dead = false;
		x = 100;
		y = 100;
		Server.getWorld().sendToAll(TCP_CMD_SPAWN, this);
	}
	
	/*
	 * network functions
	 */
	
	public void sendTCPMessage(byte[] data) {
		tcpConnection.sendPacket(data);
	}

	void sendUDPMessage(byte[] data){
		udpConnection.setPacket(data);
	}
	
	public void receiveTCPMessage(byte[] data, int len) 
	{
		try
		{
			int index = 0;
			switch(data[index++]) {
			case TCP_CMD_SHOOT:
				shootBullet(data, index);
				break;
			case TCP_CMD_GRENADE:
				throwGernade(data, index);
				break;
			case TCP_CMD_HIT:
				hitBullet(data, index);
				break;
			case TCP_CMD_DEAD:
				die();
				break;
			}
		} catch (Exception e)
		{
			//TODO print error
			//System.out.println("TCP messsage from client " + id + " caused error.\n msg: " + in);
			//don't crash
		}
	}
	
	public void receiveUDPMessage(byte[] data, int index) {
		try
		{
			switch (data[index++]) {
			case UDP_CMD_POSITION://update position
				updatePlayerPos(data, index);
				break;
			}
		} catch (Exception e)
		{
			//TODO print error 
			//System.out.println("UDP messsage from client " + id + " caused error.\n msg: " + in);
			//don't crash
		}
	}
	
	public void close() 
	{
		try
		{
			tcpConnection.close();
			udpConnection.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
