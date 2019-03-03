package eg.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import eg.game.net.interfaces.IUDPReceiver;
import eg.utils.GlobalConstants;

class UDPClient extends Thread {
	private final DatagramPacket receivePacket;
	private DatagramPacket sendPacket;
	private DatagramSocket clientSocket;
	private IUDPReceiver client;
	
	UDPClient(IUDPReceiver client, String ip, int localPort, int externalPort) throws UnknownHostException, SocketException
	{
		byte[] sendData = new byte[GlobalConstants.UDP_PACKET_SIZE];
		byte[] receiveData = new byte[GlobalConstants.UDP_PACKET_SIZE];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket = new DatagramSocket(localPort);
	    sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), externalPort);
	    this.client = client;
		start();
	}
	
	@Override
	public void run()
	{
		//loop to receive messages over udp
		while (true)
		{
			try 
			{
				clientSocket.receive(receivePacket);
				client.onNewUDPMessage(receivePacket.getData(), receivePacket.getLength());
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	//package wide access
	void sendPacket(byte[] msg)
	{
		//send a message over udp
	    try 
	    {
	    	sendPacket.setData(msg);
			clientSocket.send(sendPacket);
		} catch (IOException e) 
	    {
			e.printStackTrace();
		}
	}
}
