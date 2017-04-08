package eg.game.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable
{
	private static final int PACKET_SIZE = 24;
	
	private Socket socket;
	private DataOutputStream outToServer;
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
		sendData = new byte[PACKET_SIZE];
    	receiveData = new byte[PACKET_SIZE];
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
			outToServer = new DataOutputStream(socket.getOutputStream());
			
			//UDP
			clientSocket = new DatagramSocket(socket.getLocalPort() + 1);
	    	ServerAddress = InetAddress.getByName("localhost");
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
		BufferedReader inFromServer;
		
		try 
		{
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//loop to reciece tcp messages
			String in;
			while (!socket.isClosed() && (in = inFromServer.readLine()) != null)
				clientProxy.receivedTCPMessage(in);
			
			inFromServer.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	//package wide access
	void sendTCPMessage(String msg)
	{
		//send a message over tcp
		try 
		{
			outToServer.write((msg + '\n').getBytes());
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//package wide access
	void sendUDPMessage(String msg)
	{
		//send a message over udp
	    try 
	    {
	    	sendData = null;
	    	sendData = msg.getBytes();
	    	
	    	sendPacket.setData(sendData);
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
					clientProxy.receivedUDPMessage(new String(receivePacket.getData()).trim());
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
