package eg.game.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import eg.utils.GlobalConstants;

public class Client implements Runnable
{
	private Socket socket;
	private OutputStream outToServer;
	private byte[] sendData;
    private byte[] receiveData;
    private DatagramSocket clientSocket;
    private InetAddress ServerAddress;
    private DatagramPacket sendPacket;
    private ClientProxy clientProxy;
    private String ip;
    private int port;
    
    //cannot access outside of package
	Client (String ip, int port)
	{
		sendData = new byte[GlobalConstants.UDP_PACKET_SIZE];
    	receiveData = new byte[GlobalConstants.UDP_PACKET_SIZE];
    	clientProxy = new ClientProxy(this);
    	this.ip = ip;
    	this.port = port;
	}
	
	void connect()
	{
		try 
		{
			//TCP
			socket = new Socket(ip, port);
			socket.setTcpNoDelay(true);
			outToServer = socket.getOutputStream();
			
			//UDP
			clientSocket = new DatagramSocket(socket.getLocalPort() + 1);
	    	ServerAddress = InetAddress.getByName(ip);
		    sendPacket = new DatagramPacket(sendData, sendData.length, ServerAddress, port + 1);
		    new Thread(new UDPRecieve()).start();
		} catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		new Thread(this).start();
	}
	
	@Override
	public void run() 
	{
		InputStream inFromServer;
		
		try 
		{
			inFromServer = socket.getInputStream();
			
			//loop to reciece tcp messages
			byte[] data = new byte[GlobalConstants.TCP_PACKET_SIZE];
			while (!socket.isClosed() && inFromServer.read(data) != -1)
				clientProxy.receivedTCPMessage(data);
			
			inFromServer.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	//package wide access
	void sendTCPMessage(byte[] data)
	{
		//send a message over tcp
		try 
		{
			byte[] buffer = new byte[GlobalConstants.TCP_PACKET_SIZE];
	    	for (int i = 0; i < data.length && i < buffer.length; i++)
	    		buffer[i] = data[i];
			outToServer.write(buffer);
			outToServer.flush();
		} catch (IOException e) 
		{
			e.printStackTrace();
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
	
	private class UDPRecieve implements Runnable
	{
		private DatagramPacket receivePacket;
		
		public UDPRecieve() 
		{
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
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
					clientProxy.receivedUDPMessage(receivePacket.getData(), receivePacket.getLength());
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	ClientProxy getProxy() 
	{
		return clientProxy;
	}
}
