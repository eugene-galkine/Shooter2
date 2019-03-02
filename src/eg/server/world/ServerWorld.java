package eg.server.world;

import java.net.DatagramPacket;
import java.net.Socket;

import eg.utils.ByteArrayUtils;

public class ServerWorld 
{
	private static final int MAX_PLAYERS = 8;
	
	private ServerPlayer[] players;
	private boolean[] playerIDs;
	private int numPlayers;
	
	enum MsgType
	{
		NEW_PLAYER, //0
		REMOVE_PLAYER, //1
		UPDATE_POS, //2
		SHOOT, //3
		SPAWN,  //4
		THROW_GERNADE
	}
	
	public ServerWorld()
	{
		players = new ServerPlayer[MAX_PLAYERS];
		numPlayers = 0;
		playerIDs = new boolean[MAX_PLAYERS];
		
		for (int i = 0; i < MAX_PLAYERS; i++)
			playerIDs[i] = true;
	}
	
	private int getNextID()
	{
		for (int i = 0; i < MAX_PLAYERS; i++)
			if (playerIDs[i])
			{
				playerIDs[i] = false;
				return i;
			}
		
		return -1;
	}
	
	public synchronized void removePlayer(ServerPlayer sp) 
	{
		if (sp == null)
			return;
		
		System.out.println("removing player with id: " + sp.getID());
		
		//free the id
		playerIDs[sp.getID()] = true;
		//remove from list
		players[sp.getID()] = null;
		//close net sockets
		sp.close();
		
		numPlayers--;
		
		//tell everyone else about it
		sendToAll(MsgType.REMOVE_PLAYER, sp);
	}
	
	public synchronized ServerPlayer addPlayer(Socket socket)
	{
		if (numPlayers < MAX_PLAYERS)
		{
			ServerPlayer sp = new ServerPlayer(socket, getNextID());
			System.out.println("new player connected: " + socket.getLocalAddress() + " and was given id: " + sp.getID());
			
			//tell everyone someone has joined
			sendToAll(MsgType.NEW_PLAYER, sp, true);
			
			//add player to list
			players[sp.getID()] = sp;
			
			sp.respawn();
			
			//send data about all players to new player
			for (ServerPlayer player : players)
				if (player != null && sp != player)
					sendTo(MsgType.NEW_PLAYER, sp, player);
			
			numPlayers++;
			
			return sp;
		} else
		{
			new ServerPlayer(socket, -1).close();
			
			return null;
		}
	}
	
	public void sendToAll(MsgType mt, ServerPlayer currentPlayer)
	{
		sendToAll(mt, currentPlayer, false);
	}
	
	public synchronized void sendToAll(MsgType mt, ServerPlayer currentPlayer, boolean allButSelf)
	{
		for (ServerPlayer player : players)
		{
			if (player == null || (allButSelf && player == currentPlayer))
				continue;
			
			sendTo(mt, player, currentPlayer);
		}
	}
	
	public void sendTo(MsgType mt, ServerPlayer player, ServerPlayer currentPlayer)
	{
		switch (mt)
		{
		case NEW_PLAYER:
			player.sendTCPMessage("NEW_PLAYER|"+currentPlayer.getID());
			break;
		case REMOVE_PLAYER:
			player.sendTCPMessage("REMOVE_PLAYER|"+currentPlayer.getID());
			break;
		case UPDATE_POS:
			player.sendUDPMessage("UPD|"+currentPlayer.getID()+","+currentPlayer.getX()+","+currentPlayer.getY()+","+currentPlayer.getRot()+",");
			break;
		case SHOOT:
			player.sendTCPMessage("SHOOT|"+currentPlayer.getID()+","+currentPlayer.getfX()+","+currentPlayer.getfY()+","+currentPlayer.getfRot()+",");
			break;
		case SPAWN:
			if (player != currentPlayer)
				player.sendUDPMessage("UPD|"+currentPlayer.getID()+","+currentPlayer.getX()+","+currentPlayer.getY()+","+currentPlayer.getRot()+",");
			else
				player.sendTCPMessage("SPAWN|"+currentPlayer.getX()+","+currentPlayer.getY()+","+currentPlayer.getHealth()+",");
			
			break;
		case THROW_GERNADE:
			player.sendTCPMessage("GERNADE|"+currentPlayer.getID()+","+currentPlayer.getfX()+","+currentPlayer.getfY()+","+currentPlayer.getfRot()+",");
			break;
		}
	}
	
	public void receiveUDPMessage(byte[] data) {
		//get player ID from message
		int index = ByteArrayUtils.parseInt(data, 0);
		ServerPlayer player = null;
		try {
			//in case it's not a valid number
			player = players[index];
		} catch (Exception e) {
			System.out.println("bad udp message: " + index);
		}
		
		if (player != null)
			//throw new NullPointerException();
			player.receiveUDPMessage(data, 4);
	}
}
