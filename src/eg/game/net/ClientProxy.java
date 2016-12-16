package eg.game.net;

import eg.game.world.World;

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
	void receivedTCPMessage(String msg)
	{
		//a new message was received over tcp
		System.out.println("message from server tcp: " + msg);
		if (msg.startsWith("CONNECTED|"))
		{
			msg = msg.substring("CONNECTED|".length());
			ID = Integer.parseInt(msg);
			
		} else if (msg.startsWith("NEW_PLAYER|"))
		{
			msg = msg.substring("NEW_PLAYER|".length());
			World.getInstance().newPlayer(Integer.parseInt(msg));
			
		} else if (msg.startsWith("REMOVE_PLAYER|"))
		{
			msg = msg.substring("REMOVE_PLAYER|".length());
			World.getInstance().removePlayer(Integer.parseInt(msg));
			
		} else if (msg.startsWith("REJECTED"))
		{
			System.out.println("Server rejected connection");
		}
	}
	
	//package wide access
	void receivedUDPMessage(String msg)
	{
		//a new message was received over udp
		//System.out.println("message from server udp: " + msg);
		
		if (msg.startsWith("UPD|"))
		{
			//update position
			msg = msg.substring("UPD|".length());
			int id = Integer.parseInt(msg.substring(0, msg.indexOf(',')));
			msg = msg.substring(msg.indexOf(',') + 1);
			int x = Integer.parseInt(msg.substring(0, msg.indexOf(',')));
			msg = msg.substring(msg.indexOf(',') + 1);
			int y = Integer.parseInt(msg.substring(0, msg.indexOf(',')));
			msg = msg.substring(msg.indexOf(',') + 1);
			int rot = Integer.parseInt(msg.substring(0, msg.indexOf(',')));
			
			World.getInstance().updatePlayer(id, x, y, rot);
		} else if (msg.startsWith("SHOOT|"))
		{
			msg = msg.substring("SHOOT|".length());
			int id = Integer.parseInt(msg.substring(0, msg.indexOf(',')));
			msg = msg.substring(msg.indexOf(',') + 1);
			int shootRot = Integer.parseInt(msg.substring(0, msg.indexOf(',')));
			
			World.getInstance().netShoot(id, shootRot);
		}
	}
	
	public void updatePlayerPos(int x, int y, int rot)
	{
		client.sendUDPMessage("POS|"+ID+","+x+","+y+","+rot+",");
	}

	public void shoot(int rot) 
	{
		client.sendUDPMessage("SHOOT|"+ID+","+rot+",");
	}

	public void bulletHit(int targetID, int ownerID, int weaponID) 
	{
		client.sendTCPMessage("HIT|"+ownerID+","+weaponID+",");
		//System.out.println("player " + targetID + " took damage from " + ownerID + " (ClientProxy)");
	}
	
	public void dead() 
	{
		client.sendTCPMessage("DEAD|,");
	}
	
	public int getID() 
	{
		return ID;
	}
}
