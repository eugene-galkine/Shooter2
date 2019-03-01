package eg.server;

import eg.server.net.Server;


public class ServerMain 
{	
	public static void main(String argv[]) throws Exception
	{
		(new Thread(new Server())).start();
	}
}
