package eg.server.net;

import static eg.utils.GlobalConstants.TCP_PACKET_SIZE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import eg.server.world.ServerPlayer;
import eg.utils.GlobalConstants;

public class TCPConnection extends Thread
{
	private Socket socket;
	private OutputStream outputStream;
	
	public TCPConnection (Socket s)
	{
		socket = s;
		start();
	}
	
	@Override
	public void run() 
	{
		ServerPlayer sp = null;
		
		try
		{
			InputStream inFromClient = socket.getInputStream();
			outputStream = socket.getOutputStream();
			sp = Server.getWorld().addPlayer(this, new UDPConnection(socket.getInetAddress(), socket.getPort()));
			
			if (sp == null)
			{
				//server was full
				socket.close();
				return;
			}
			
			byte[] data = new byte[GlobalConstants.TCP_PACKET_SIZE];
			int len;
			while (!socket.isClosed() && (len = inFromClient.read(data)) != -1)
				sp.receiveTCPMessage(data, len);
			
			inFromClient.close();
			//obj.close();
		} catch (Exception e) 
		{
			Server.getWorld().removePlayer(sp);
			
			return;
		}
	}
	
	public void sendPacket(byte[] data) 
	{
		try 
		{
			byte[] buffer = new byte[TCP_PACKET_SIZE];
	    	for (int i = 0; i < data.length && i < buffer.length; i++)
	    		buffer[i] = data[i];
			outputStream.write(buffer);//TODO move to TCPConnection
			outputStream.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		outputStream.close();
		socket.close();
	}
}
