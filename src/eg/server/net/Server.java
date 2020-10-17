package eg.server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import eg.server.world.ServerWorld;

public class Server implements Runnable
{
	private static final int PORT = 1426;
	private static ServerWorld sw;

	public Server() {
	}

	@Override
	public void run() 
	{
		sw = new ServerWorld();

		try {
			TCPServer tcpServer =  new TCPServer(PORT);
			UDPServer server = new UDPServer(PORT + 1);
			System.out.println("Game Server started on port: " + tcpServer.getLocalPort() + " and: " + server.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ServerWorld getWorld()
	{
		return sw;
	}
}
