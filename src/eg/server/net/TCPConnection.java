package eg.server.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import eg.server.world.ServerPlayer;

public class TCPConnection extends Thread
{
	private Socket socket;
	
	public TCPConnection (Socket s)
	{
		socket = s;
		start();
	}
	
	@Override
	public void run() 
	{
		ServerPlayer sp = null;
		
		try
		{
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			sp = Server.getWorld().addPlayer(socket);
			
			if (sp == null)
			{
				//server was full
				socket.close();
				return;
			}
			
			String in;
			while (!socket.isClosed() && (in = inFromClient.readLine()) != null)
				sp.receiveTCPMessage(in);
			
			inFromClient.close();
			//obj.close();
		} catch (Exception e) 
		{
			Server.getWorld().removePlayer(sp);
			
			return;
		}
	}
}
