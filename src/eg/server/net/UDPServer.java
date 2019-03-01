package eg.server.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread
{
	public static final int PACKET_SIZE = 24;
	
	private DatagramPacket receivePacket;
	private byte[] receiveData;
	private DatagramSocket udpSocket;
	
	public UDPServer(int port) throws SocketException
	{
		receiveData = new byte[PACKET_SIZE];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		udpSocket = new DatagramSocket(port);
		start();
	}

	int getLocalPort() 
	{
		return udpSocket.getLocalPort();
	}
	
	@Override
	public void run() 
	{
		try
		{
			while (true)
			{
				udpSocket.receive(receivePacket);
				Server.getWorld().receiveUDPMessage(receivePacket);
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}