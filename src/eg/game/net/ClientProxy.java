package eg.game.net;

import eg.game.state.mpShooter.GameWorld;

public class ClientProxy 
{
	
	
	private static final byte CMD_SHOOT = 0;
	private static final byte CMD_GRENADE = 1;
	private static final byte CMD_NEW_PLAYER = 2;
	private static final byte CMD_REMOVE_PLAYER = 3;
	private static final byte CMD_SPAWN = 4;
	private static final byte CMD_REJECTED = 5;
	private static final byte CMD_CONNECTED = 6;
	
	
	
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
		case CMD_SHOOT://shoot
			id = parseInt(msg, position);
			position += 4;
			x = parseFloat(msg, position);
			position += 4;
			y = parseFloat(msg, position);
			position += 4;
			shootRot = parseFloat(msg, position);
			
			GameWorld.getInstance().netShoot(id, x, y, shootRot);
			break;
		case CMD_GRENADE://grenade
			id = parseInt(msg, position);
			position += 4;
			x = parseFloat(msg, position);
			position += 4;
			y = parseFloat(msg, position);
			position += 4;
			rot = parseFloat(msg, position);
			
			GameWorld.getInstance().throwGernade(id, x, y, rot);
			break;
		case CMD_NEW_PLAYER://new player
			GameWorld.getInstance().newPlayer(parseInt(msg, position));
			break;
		case CMD_REMOVE_PLAYER://remove player
			GameWorld.getInstance().removePlayer(parseInt(msg, position));
			break;
		case CMD_SPAWN://spawn
			iX = parseInt(msg, position);
			position += 4;
			iY = parseInt(msg, position);
			position += 4;
			int health = parseInt(msg, position);
			
			GameWorld.getInstance().spawnPlayer(iX,iY,health);
			break;
		case CMD_REJECTED://rejected
			System.out.println("Server rejected connection");
			//TODO rejected connection from server
			break;
		case CMD_CONNECTED://connected
			ID = parseInt(msg, position);
			break;
		}
	}
	
	private int parseInt(byte[] data, int pos) {
		//TODO check for errors
		int result = 0;
		result |= data[pos] << 24;
		result |= (data[pos + 1] & 0xff) << 16;
		result |= (data[pos + 2] & 0xff) << 8;
		result |= (data[pos + 3] & 0xff);
		
		return result;
	}
	
	private float parseFloat(byte[] data, int pos) {
		//TODO check for errors
		return Float.intBitsToFloat(
				data[pos] << 24 |
				(data[pos + 1] & 0xff) << 16 |
				(data[pos + 2] & 0xff) << 8 |
				(data[pos + 3] & 0xff));
	}
	
	private byte[] appendInt(byte[] data, int index, int msg) {	
		data[index] = (byte)(msg >> 0);
		data[index + 1] = (byte)(msg >> 8);
		data[index + 2] = (byte)(msg >> 16);
		data[index + 3] = (byte)(msg >> 24);
		
		return data;
	}
	
	private byte[] appendFloat(byte[] data, int index, float msg) {	
		int converted = Float.floatToIntBits(msg);
		data[index] = (byte)(converted >> 0);
		data[index + 1] = (byte)(converted >> 8);
		data[index + 2] = (byte)(converted >> 16);
		data[index + 3] = (byte)(converted >> 24);
		
		return data;
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
			id = parseInt(msg, position);
			position += 4;
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
		byte[] data = new byte[1 + 4 + 4 + 4 + 4];
		data[0] = 0;
		data = appendInt(data, 4, ID);
		data = appendInt(data, 8, x);
		data = appendInt(data, 12, y);
		data = appendInt(data, 16, rot);
		
		client.sendUDPMessage(data, data.length);
		//"POS|"+ID+","+x+","+y+","+rot+",");
	}

	public void shoot(float x, float y, float rot) 
	{
		byte[] data = new byte[1 + 4 + 4 + 4];
		data[0] = 1;
		data = appendFloat(data, 4, x);
		data = appendFloat(data, 8, y);
		data = appendFloat(data, 12, rot);
		
		client.sendUDPMessage(data, data.length);
		//client.sendTCPMessage("SHOOT|"+x+","+y+","+rot+",");
	}

	public void bulletHit(int targetID, int ownerID, int weaponID) 
	{
		byte[] data = new byte[1 + 4 + 4];
		data[0] = 2;
		data = appendInt(data, 4, ownerID);
		data = appendInt(data, 8, weaponID);
		
		client.sendUDPMessage(data, data.length);
		//client.sendTCPMessage("HIT|"+ownerID+","+weaponID+",");
		//System.out.println("player " + targetID + " took damage from " + ownerID + " (ClientProxy)");
	}
	
	public void dead() 
	{
		byte[] data = new byte[1];
		data[0] = 3;
		
		client.sendUDPMessage(data, data.length);
		//client.sendTCPMessage("DEAD|,");
	}
	
	public void throwGernade(float x, float y, float dir)
	{
		byte[] data = new byte[1];
		data[0] = 4;
		data = appendFloat(data, 4, x);
		data = appendFloat(data, 8, y);
		data = appendFloat(data, 12, dir);;
		
		client.sendUDPMessage(data, data.length);
		//client.sendTCPMessage("GERNADE|"+x+","+y+","+dir+",");
	}
}
