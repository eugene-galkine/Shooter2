package eg.game.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import eg.game.net.interfaces.IUDPReceiver;
import eg.utils.GlobalConstants;

public class Client implements Runnable, IUDPReceiver
{
	private Socket socket;
	private OutputStream outToServer;
    private ClientProxy clientProxy;
    private String ip;
    private int port;
    private UDPClient udpClient;
    
    //cannot access outside of package
	Client (String ip, int port) {
    	clientProxy = new ClientProxy(this);
    	this.ip = ip;
    	this.port = port;
	}
	
	void connect() {
		try {
			//TCP
			socket = new Socket(ip, port);
			socket.setTcpNoDelay(true);
			outToServer = socket.getOutputStream();
			
			//UDP
			udpClient = new UDPClient(this, ip, socket.getLocalPort() + 1);
		} catch (IOException e) {
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

	public void sendUDPMessage(byte[] data) {
		udpClient.sendPacket(data);
	}

	@Override
	public void onNewUDPMessage(byte[] data, int length) {
		clientProxy.receivedUDPMessage(data, length);
	}
}
