package eg.server.world;

import eg.server.interfaces.Synchronized;
import eg.server.net.TCPConnection;
import eg.server.net.UDPConnection;
import eg.utils.ByteArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static eg.utils.GlobalConstants.*;
import static eg.utils.ByteArrayUtils.*;

public class ServerWorld {
	private static final int MAX_PLAYERS = 8;
	
	private final ServerPlayer[] players = new ServerPlayer[MAX_PLAYERS];
	private final HashMap<Integer, Synchronized> worldObjects = new HashMap<>();

	private int getNextID() {
		for (int i = 0; i < MAX_PLAYERS; i++)
			if (players[i] == null)
				return i;
		
		return -1;
	}
	
	public synchronized void removePlayer(ServerPlayer sp) {
		if (sp == null)
			return;
		
		System.out.println("removing player with id: " + sp.getID());
		players[sp.getID()] = null;
		sp.close();
		
		//TODO trigger deletion of the player object ---- sendToAll(TCP_CMD_REMOVE_PLAYER, sp);
	}
	
	public synchronized ServerPlayer addPlayer(TCPConnection tcp, UDPConnection udp) {
		ServerPlayer sp = new ServerPlayer(tcp, udp, getNextID());

		if (sp.getID() != -1) {
			System.out.println("new player connected and was given id: " + sp.getID());

			//TODO trigger creation of the player object ---- sendToAll(TCP_CMD_NEW_PLAYER, sp, true);
			
			players[sp.getID()] = sp;
			
			sp.respawn(); //TODO remove
			sp.sync(worldObjects);//send data about all players to new player
			return sp;
		} else {
			sp.close();
			return null;
		}
	}
	
	void sendToAll(byte type, ServerPlayer currentPlayer) {
		sendToAll(type, currentPlayer, false);
	}
	
	synchronized void sendToAll(byte type, ServerPlayer currentPlayer, boolean allButSelf) {
		for (ServerPlayer player : players) {
			if (player == null || (allButSelf && player == currentPlayer))
				continue;
			
			sendTo(type, player, currentPlayer);
		}
	}

	//TODO remove this switch statement and used the synchronized interface instead
	private void sendTo(byte type, ServerPlayer player, ServerPlayer currentPlayer)
	{
		byte[] data;
		switch (type)
		{
		case TCP_CMD_NEW_PLAYER:
			data = new byte[5];
			data[0] = TCP_CMD_NEW_PLAYER;
			appendInt(data, 1, currentPlayer.getID());
			player.sendTCPMessage(data);
			//player.sendTCPMessage("NEW_PLAYER|"+currentPlayer.getID());
			break;
		case TCP_CMD_REMOVE_PLAYER:
			data = new byte[5];
			data[0] = TCP_CMD_REMOVE_PLAYER;
			appendInt(data, 1, currentPlayer.getID());
			player.sendTCPMessage(data);
			//player.sendTCPMessage("REMOVE_PLAYER|"+currentPlayer.getID());
			break;
		case UDP_CMD_POSITION:
			data = new byte[1 + 4 + 4 + 4 + 4];
			appendInt(data, 0, currentPlayer.getID());
			data[4] = UDP_CMD_POSITION;
			appendInt(data, 5, currentPlayer.getX());
			appendInt(data, 9, currentPlayer.getY());
			appendInt(data, 13, currentPlayer.getRot());
			player.sendUDPMessage(data);
			//player.sendUDPMessage("UPD|"+currentPlayer.getID()+","+currentPlayer.getX()+","+currentPlayer.getY()+","+currentPlayer.getRot()+",");
			break;
		case TCP_CMD_SHOOT:
			data = new byte[1 + 4 + 4 + 4 + 4];
			data[0] = TCP_CMD_SHOOT;
			appendInt(data, 1, currentPlayer.getID());
			appendFloat(data, 5, currentPlayer.getfX());
			appendFloat(data, 9, currentPlayer.getfY());
			appendFloat(data, 13, currentPlayer.getfRot());
			player.sendTCPMessage(data);
			//player.sendTCPMessage("SHOOT|"+currentPlayer.getID()+","+currentPlayer.getfX()+","+currentPlayer.getfY()+","+currentPlayer.getfRot()+",");
			break;
		case TCP_CMD_SPAWN:
			if (player != currentPlayer) {
				data = new byte[1 + 4 + 4 + 4 + 4];
				appendInt(data, 0, currentPlayer.getID());
				data[4] = UDP_CMD_POSITION;
				appendInt(data, 5, currentPlayer.getX());
				appendInt(data, 9, currentPlayer.getY());
				appendInt(data, 13, currentPlayer.getRot());
				player.sendUDPMessage(data);
//				player.sendUDPMessage("UPD|"+currentPlayer.getID()+","+currentPlayer.getX()+","+currentPlayer.getY()+","+currentPlayer.getRot()+",");
			} else {
				data = new byte[1 + 4 + 4 + 4];
				data[0] = TCP_CMD_SPAWN;
				appendInt(data, 1, currentPlayer.getX());
				appendInt(data, 5, currentPlayer.getY());
				appendInt(data, 9, currentPlayer.getHealth());
				player.sendTCPMessage(data);
//				player.sendTCPMessage("SPAWN|"+currentPlayer.getX()+","+currentPlayer.getY()+","+currentPlayer.getHealth()+",");
			}
			
			break;
		case TCP_CMD_GRENADE:
			data = new byte[1 + 4 + 4 + 4 + 4];
			data[0] = TCP_CMD_GRENADE;
			appendInt(data, 1, currentPlayer.getID());
			appendFloat(data, 5, currentPlayer.getfX());
			appendFloat(data, 9, currentPlayer.getfY());
			appendFloat(data, 13, currentPlayer.getfRot());
			player.sendTCPMessage(data);
//			player.sendTCPMessage("GRENADE|"+currentPlayer.getID()+","+currentPlayer.getfX()+","+currentPlayer.getfY()+","+currentPlayer.getfRot()+",");
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
			System.out.println("invalid id in udp message: " + index);
		}
		
		if (player != null)
			//throw new NullPointerException();
			player.receiveUDPMessage(data, 4);
	}
}
