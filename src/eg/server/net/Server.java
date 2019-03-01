package eg.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import eg.server.world.ServerWorld;

public class Server implements Runnable
{
	private static final int PORT = 1426;
	
	private static ServerWorld sw;
	
	private ServerSocket serverSocket;	
	
	
	public Server()
	{
	}

	@Override
	public void run() 
	{
		try {
			serverSocket = new ServerSocket(PORT);
			
			UDPServer server = new UDPServer(PORT + 1);
			System.out.println("Game Server started on port: " + serverSocket.getLocalPort() + " and: " + server.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sw = new ServerWorld();
		
		while(true)
		{
			try
			{
				Socket connectionSocket = serverSocket.accept();
				connectionSocket.setTcpNoDelay(true);
				new TCPConnection(connectionSocket);
		    } catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public static ServerWorld getWorld()
	{
		return sw;
	}
}
