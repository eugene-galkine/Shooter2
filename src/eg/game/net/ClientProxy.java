package eg.game.net;

import eg.game.state.mpShooter.GameWorld;

import static eg.utils.GlobalConstants.*;
import static eg.utils.ByteArrayUtils.*;

public class ClientProxy 
{
	private final Client client;
	private int ID;
	
	ClientProxy(Client c)
	{
		this.client = c;
		this.ID = -1;
	}
	
	public void start()
	{
		client.connect();
	}
	
	//package wide access
	void receivedTCPMessage(byte[] msg)
	{
		//a new message was received over tcp
		//System.out.println("message from server tcp: " + msg[0]);
		
		int position = 0;
		int id, iX, iY;
		float x, y, shootRot, rot;
		switch(msg[position++]) {
		case TCP_CMD_SHOOT://shoot
			id = parseInt(msg, position);
			position += 4;
			x = parseFloat(msg, position);
			position += 4;
			y = parseFloat(msg, position);
			position += 4;
			shootRot = parseFloat(msg, position);
			
			GameWorld.getInstance().netShoot(id, x, y, shootRot);
			break;
		case TCP_CMD_GRENADE://grenade
			id = parseInt(msg, position);
			position += 4;
			x = parseFloat(msg, position);
			position += 4;
			y = parseFloat(msg, position);
			position += 4;
			rot = parseFloat(msg, position);
			
			GameWorld.getInstance().throwGernade(id, x, y, rot);
			break;
		case TCP_CMD_NEW_PLAYER://new player
			GameWorld.getInstance().newPlayer(parseInt(msg, position));
			break;
		case TCP_CMD_REMOVE_PLAYER://remove player
			GameWorld.getInstance().removePlayer(parseInt(msg, position));
			break;
		case TCP_CMD_SPAWN://spawn
			iX = parseInt(msg, position);
			position += 4;
			iY = parseInt(msg, position);
			position += 4;
			int health = parseInt(msg, position);
			
			GameWorld.getInstance().spawnPlayer(iX,iY,health);
			break;
		case TCP_CMD_REJECTED://rejected
			System.out.println("Server rejected connection");
			//TODO rejected connection from server
			break;
		case TCP_CMD_CONNECTED://connected
			ID = parseInt(msg, position);
			break;
		}
	}
	
	public int getID() 
	{
		return ID;
	}
	
	//package wide access
	void receivedUDPMessage(byte[] msg, int length)
	{
		//a new message was received over udp
//		System.out.println("message from server udp: " + msg[0]);
		
		int position = 0;
		int id = parseInt(msg, position);
		position += 4;
		int x, y, rot;
		switch(msg[position++]) {
		case UDP_CMD_POSITION:
			//update position
			x = parseInt(msg, position);
			position += 4;
			y = parseInt(msg, position);
			position += 4;
			rot = parseInt(msg, position);
			
			GameWorld.getInstance().updatePlayer(id, x, y, rot);
			break;
		}
	}
	
	public void updatePlayerPos(int x, int y, int rot)
	{
		byte[] data = new byte[4 + 1 + 4 + 4 + 4];
		appendInt(data, 0, ID);
		data[4] = UDP_CMD_POSITION;
		appendInt(data, 5, x);
		appendInt(data, 9, y);
		appendInt(data, 13, rot);

		client.sendUDPMessage(data);
		//"POS|"+ID+","+x+","+y+","+rot+",");
	}

	public void shoot(float x, float y, float rot) 
	{
		byte[] data = new byte[1 + 4 + 4 + 4];
		data[0] = TCP_CMD_SHOOT;
		appendFloat(data, 1, x);
		appendFloat(data, 5, y);
		appendFloat(data, 9, rot);

		client.sendTCPMessage(data);
		//client.sendTCPMessage("SHOOT|"+x+","+y+","+rot+",");
	}

	public void throwGernade(float x, float y, float dir)
	{
		byte[] data = new byte[1 + 4 + 4 + 4];
		data[0] = TCP_CMD_GRENADE;
		appendFloat(data, 1, x);
		appendFloat(data, 5, y);
		appendFloat(data, 9, dir);

		client.sendTCPMessage(data);
		//client.sendTCPMessage("GERNADE|"+x+","+y+","+dir+",");
	}
	
	public void bulletHit(int targetID, int ownerID, int weaponID) 
	{
		byte[] data = new byte[1 + 4 + 4];
		data[0] = TCP_CMD_HIT;
		appendInt(data, 1, ownerID);
		appendInt(data, 5, weaponID);

		client.sendTCPMessage(data);
		//client.sendTCPMessage("HIT|"+ownerID+","+weaponID+",");
		//System.out.println("player " + targetID + " took damage from " + ownerID + " (ClientProxy)");
	}
	
	public void dead() 
	{
		byte[] data = new byte[1];
		data[0] = TCP_CMD_DEAD;
		
		client.sendTCPMessage(data);
		//client.sendTCPMessage("DEAD|,");
	}
}
