package eg.server.net;

import static eg.utils.GlobalConstants.TCP_PACKET_SIZE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import eg.server.world.ServerPlayer;
import eg.utils.GlobalConstants;

public class TCPConnection extends Thread {
	private final Socket socket;
	private OutputStream outputStream;
	private InputStream inFromClient;
	
	public TCPConnection (Socket s) {
		socket = s;
		start();
	}
	
	@Override
	public void run() {
		ServerPlayer sp = null;
		
		try {
			inFromClient = socket.getInputStream();
			outputStream = socket.getOutputStream();//TODO maybe make a factory instead of doing this ... ?
			sp = Server.getWorld().addPlayer(this, new UDPConnection(socket.getInetAddress(), socket.getPort()));
			
			if (sp == null)
				return;
			
			byte[] data = new byte[GlobalConstants.TCP_PACKET_SIZE];
			int len;
			while (!socket.isClosed() && (len = inFromClient.read(data)) != -1)
				sp.receiveTCPMessage(data, len);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Server.getWorld().removePlayer(sp);
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendPacket(byte[] data) {
		try {
			byte[] buffer = new byte[TCP_PACKET_SIZE];
	    	for (int i = 0; i < data.length && i < buffer.length; i++)
	    		buffer[i] = data[i];
			outputStream.write(buffer);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		if (outputStream != null)
			outputStream.close();
		if (inFromClient != null)
			inFromClient.close();
		socket.close();
	}
}
