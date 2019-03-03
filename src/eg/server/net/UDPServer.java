package eg.server.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import eg.utils.GlobalConstants;

public class UDPServer extends Thread
{
	private DatagramPacket receivePacket;
	private byte[] receiveData;
	private DatagramSocket udpSocket;
	
	public UDPServer(int port) throws SocketException
	{
		receiveData = new byte[GlobalConstants.UDP_PACKET_SIZE];
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
				Server.getWorld().receiveUDPMessage(receivePacket.getData());
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
