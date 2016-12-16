package eg.game.net;

public class ClientFactory 
{
	public static ClientProxy connectToServer(String ip, int port)
	{
		//connect to the server and return the proxy to access the client
		Client c = new Client(ip, port);
		
		return c.getProxy();
	}
}
