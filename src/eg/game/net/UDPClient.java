package eg.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import eg.game.net.interfaces.IUDPReceiver;
import eg.utils.GlobalConstants;

public class UDPClient extends Thread {
	private DatagramPacket receivePacket;
	private DatagramPacket sendPacket;
	private DatagramSocket clientSocket;
	private IUDPReceiver client;
	
	UDPClient(IUDPReceiver client, String ip, int port) throws UnknownHostException, SocketException
	{
		byte[] sendData = new byte[GlobalConstants.UDP_PACKET_SIZE];
		byte[] receiveData = new byte[GlobalConstants.UDP_PACKET_SIZE];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket = new DatagramSocket(port);
	    sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port + 1);
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
