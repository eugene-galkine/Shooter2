package eg.server;

import eg.server.net.Server;


class ServerMain
{	
	public static void main(String[] argv) {
		(new Thread(new Server())).start();
	}
}
