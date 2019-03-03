package eg.server.net;

import java.io.InputStream;
import java.net.Socket;

import eg.server.world.ServerPlayer;
import eg.utils.GlobalConstants;

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
			InputStream inFromClient = socket.getInputStream();
			sp = Server.getWorld().addPlayer(socket);
			
			if (sp == null)
			{
				//server was full
				socket.close();
				return;
			}
			
			byte[] data = new byte[GlobalConstants.TCP_PACKET_SIZE];
			int len;
			while (!socket.isClosed() && (len = inFromClient.read(data)) != -1)
				sp.receiveTCPMessage(data, len);
			
			inFromClient.close();
			//obj.close();
		} catch (Exception e) 
		{
			Server.getWorld().removePlayer(sp);
			
			return;
		}
	}
}
