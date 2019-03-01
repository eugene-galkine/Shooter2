package eg.game.net;

import eg.game.state.mpShooter.GameWorld;

public class ClientProxy 
{
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
		case 0://shoot
			id = parseInt(msg, position);
			position += 4;
			x = parseFloat(msg, position);
			position += 4;
			y = parseFloat(msg, position);
			position += 4;
			shootRot = parseFloat(msg, position);
			
			GameWorld.getInstance().netShoot(id, x, y, shootRot);
			break;
		case 1://grenade
			id = parseInt(msg, position);
			position += 4;
			x = parseFloat(msg, position);
			position += 4;
			y = parseFloat(msg, position);
			position += 4;
			rot = parseFloat(msg, position);
			
			GameWorld.getInstance().throwGernade(id, x, y, rot);
			break;
		case 2://new player
			GameWorld.getInstance().newPlayer(parseInt(msg, position));
			break;
		case 3://remove player
			GameWorld.getInstance().removePlayer(parseInt(msg, position));
			break;
		case 4://spawn
			iX = parseInt(msg, position);
			position += 4;
			iY = parseInt(msg, position);
			position += 4;
			int health = parseInt(msg, position);
			
			GameWorld.getInstance().spawnPlayer(iX,iY,health);
			break;
		case 5://rejected
			System.out.println("Server rejected connection");
			//TODO rejected connection from server
			break;
		case 6://connected
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
		client.sendUDPMessage("POS|"+ID+","+x+","+y+","+rot+",");//TODO
	}

	public void shoot(float x, float y, float rot) 
	{
		client.sendTCPMessage("SHOOT|"+x+","+y+","+rot+",");//TODO
	}

	public void bulletHit(int targetID, int ownerID, int weaponID) 
	{
		client.sendTCPMessage("HIT|"+ownerID+","+weaponID+",");//TODO
		//System.out.println("player " + targetID + " took damage from " + ownerID + " (ClientProxy)");
	}
	
	public void dead() 
	{
		client.sendTCPMessage("DEAD|,");//TODO
	}
	
	public int getID() 
	{
		return ID;
	}
	
	public void throwGernade(float x, float y, float dir)
	{
		client.sendTCPMessage("GERNADE|"+x+","+y+","+dir+",");//TODO
	}
}
