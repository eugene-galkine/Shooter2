package eg.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import eg.server.world.ServerPlayer;
import eg.server.world.ServerWorld;

public class Server implements Runnable
{
	private static final int PORT = 1426;
	public static final int PACKET_SIZE = 24;
	
	private static ServerWorld sw;
	
	private ServerSocket serverSocket;	
	private DatagramSocket udpSocket;
	
	public Server()
	{
	}

	@Override
	public void run() 
	{
		try {
			serverSocket = new ServerSocket(PORT);
			udpSocket = new DatagramSocket(PORT + 1);
			new Thread(new UDPServer()).start();
			
			System.out.println("Game Server started on port: " + serverSocket.getLocalPort() + " and: " + udpSocket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		sw = new ServerWorld();
		
		while(true)
		{
			try
			{
				Socket connectionSocket = serverSocket.accept();
				new Thread(new ClaimedSocket(connectionSocket)).start();
		    } catch (Exception e) {e.printStackTrace();}
		}
	}
	
	private class ClaimedSocket implements Runnable
	{
		private Socket socket;
		
		public ClaimedSocket (Socket s)
		{
			socket = s;
		}
		
		@Override
		public void run() 
		{
			ServerPlayer sp = null;
			
			try
			{
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				sp = sw.addPlayer(socket);
				
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
				sw.removePlayer(sp);
				
				return;
			}
		}
	}
	
	private class UDPServer implements Runnable
	{
		private DatagramPacket receivePacket;
		private byte[] receiveData;
		
		public UDPServer()
		{
			receiveData = new byte[PACKET_SIZE];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
		}

		@Override
		public void run() 
		{
			try
			{
				while (true)
				{
					udpSocket.receive(receivePacket);
					sw.receiveUDPMessage(receivePacket);
				}
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static ServerWorld getWorld()
	{
		return sw;
	}
}
