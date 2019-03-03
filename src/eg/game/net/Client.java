package eg.game.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import eg.utils.GlobalConstants;

public class Client implements Runnable
{
	private Socket socket;
	private OutputStream outToServer;
    private ClientProxy clientProxy;
    private String ip;
    private int port;
    
    //cannot access outside of package
	Client (String ip, int port) {
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
		    new UDPClient(ip, port + 1);
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

	ClientProxy getProxy() 
	{
		return clientProxy;
	}
}
