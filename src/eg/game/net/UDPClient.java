package eg.game.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import eg.utils.GlobalConstants;

public class UDPClient extends Thread {
	private DatagramPacket receivePacket;
	private DatagramPacket sendPacket;
	private DatagramSocket clientSocket;
	private byte[] receiveData;
	
	public UDPClient(String ip, int port) throws UnknownHostException, SocketException 
	{
		byte[] sendData = new byte[GlobalConstants.UDP_PACKET_SIZE];
		receiveData = new byte[GlobalConstants.UDP_PACKET_SIZE];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket = new DatagramSocket(port);
	    sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ip), port + 1);
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
				clientProxy.receivedUDPMessage(receivePacket.getData(), receivePacket.getLength());//TODO good design pattern for 2 way communication
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	//package wide access
	void sendUDPMessage(byte[] msg)
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
