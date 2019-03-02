package eg.game.net;

import eg.game.state.mpShooter.GameWorld;
import eg.utils.ByteArrayUtils;

public class ClientProxy 
{
	private static final byte TCP_CMD_SHOOT = 0;
	private static final byte TCP_CMD_GRENADE = 1;
	private static final byte TCP_CMD_NEW_PLAYER = 2;
	private static final byte TCP_CMD_REMOVE_PLAYER = 3;
	private static final byte TCP_CMD_SPAWN = 4;
	private static final byte TCP_CMD_REJECTED = 5;
	private static final byte TCP_CMD_CONNECTED = 6;
	
	private Client client;
	private int ID;
	
	public ClientProxy(Client c)
	{
		this.client = c;
		this.ID = -1;
	}
	
	public void start()
	{
		client.connect();
	}
	
	//package wide access
	void receivedTCPMessage(byte[] msg, int length)//TODO use length
	{
		//a new message was received over tcp
		//System.out.println("message from server tcp: " + msg);
		
		int position = 1;
		int id, iX, iY;
		float x, y, shootRot, rot;
		switch(msg[0]) {
		case TCP_CMD_SHOOT://shoot
			id = ByteArrayUtils.parseInt(msg, position);
			position += 4;
			x = ByteArrayUtils.parseFloat(msg, position);
			position += 4;
			y = ByteArrayUtils.parseFloat(msg, position);
			position += 4;
			shootRot = ByteArrayUtils.parseFloat(msg, position);
			
			GameWorld.getInstance().netShoot(id, x, y, shootRot);
			break;
		case TCP_CMD_GRENADE://grenade
			id = ByteArrayUtils.parseInt(msg, position);
			position += 4;
			x = ByteArrayUtils.parseFloat(msg, position);
			position += 4;
			y = ByteArrayUtils.parseFloat(msg, position);
			position += 4;
			rot = ByteArrayUtils.parseFloat(msg, position);
			
			GameWorld.getInstance().throwGernade(id, x, y, rot);
			break;
		case TCP_CMD_NEW_PLAYER://new player
			GameWorld.getInstance().newPlayer(ByteArrayUtils.parseInt(msg, position));
			break;
		case TCP_CMD_REMOVE_PLAYER://remove player
			GameWorld.getInstance().removePlayer(ByteArrayUtils.parseInt(msg, position));
			break;
		case TCP_CMD_SPAWN://spawn
			iX = ByteArrayUtils.parseInt(msg, position);
			position += 4;
			iY = ByteArrayUtils.parseInt(msg, position);
			position += 4;
			int health = ByteArrayUtils.parseInt(msg, position);
			
			GameWorld.getInstance().spawnPlayer(iX,iY,health);
			break;
		case TCP_CMD_REJECTED://rejected
			System.out.println("Server rejected connection");
			//TODO rejected connection from server
			break;
		case TCP_CMD_CONNECTED://connected
			ID = ByteArrayUtils.parseInt(msg, position);
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
		//System.out.println("message from server udp: " + msg);
		
		int position = 1;
		int id, x, y, rot;
		switch(msg[0]) {
		case 0:
			//update position
			id = ByteArrayUtils.parseInt(msg, position);
			position += 4;
			x = ByteArrayUtils.parseInt(msg, position);
			position += 4;
			y = ByteArrayUtils.parseInt(msg, position);
			position += 4;
			rot = ByteArrayUtils.parseInt(msg, position);
			
			GameWorld.getInstance().updatePlayer(id, x, y, rot);
			break;
		}
	}
	
	public void updatePlayerPos(int x, int y, int rot)
	{
		byte[] data = new byte[4 + 1 + 4 + 4 + 4];
		data = ByteArrayUtils.appendInt(data, 0, ID);
		data[4] = 0;
		data = ByteArrayUtils.appendInt(data, 5, x);
		data = ByteArrayUtils.appendInt(data, 9, y);
		data = ByteArrayUtils.appendInt(data, 13, rot);
		
		client.sendUDPMessage(data, data.length);
		//"POS|"+ID+","+x+","+y+","+rot+",");
	}

	public void shoot(float x, float y, float rot) 
	{
		byte[] data = new byte[1 + 4 + 4 + 4];
		data[0] = TCP_CMD_SHOOT;//TODO
		data = ByteArrayUtils.appendFloat(data, 1, x);
		data = ByteArrayUtils.appendFloat(data, 5, y);
		data = ByteArrayUtils.appendFloat(data, 9, rot);
		
		client.sendTCPMessage(data, data.length);
		//client.sendTCPMessage("SHOOT|"+x+","+y+","+rot+",");
	}

	public void bulletHit(int targetID, int ownerID, int weaponID) 
	{
		byte[] data = new byte[1 + 4 + 4];
		data[0] = 1;//TODO
		data = ByteArrayUtils.appendInt(data, 1, ownerID);
		data = ByteArrayUtils.appendInt(data, 5, weaponID);
		
		client.sendTCPMessage(data, data.length);
		//client.sendTCPMessage("HIT|"+ownerID+","+weaponID+",");
		//System.out.println("player " + targetID + " took damage from " + ownerID + " (ClientProxy)");
	}
	
	public void dead() 
	{
		byte[] data = new byte[1];
		data[0] = 2;//TODO
		
		client.sendTCPMessage(data, data.length);
		//client.sendTCPMessage("DEAD|,");
	}
	
	public void throwGernade(float x, float y, float dir)
	{
		byte[] data = new byte[1];
		data[0] = 3;//TODO
		data = ByteArrayUtils.appendFloat(data, 1, x);
		data = ByteArrayUtils.appendFloat(data, 5, y);
		data = ByteArrayUtils.appendFloat(data, 9, dir);;
		
		client.sendTCPMessage(data, data.length);
		//client.sendTCPMessage("GERNADE|"+x+","+y+","+dir+",");
	}
}
