package eg.server.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import eg.utils.GlobalConstants;

class UDPServer extends Thread {
	private final DatagramPacket receivePacket;
	private DatagramSocket udpSocket;
	
	UDPServer(int port) throws SocketException {
		byte[] receiveData = new byte[GlobalConstants.UDP_PACKET_SIZE];
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		udpSocket = new DatagramSocket(port);
		start();
	}

	int getLocalPort() 
	{
		return udpSocket.getLocalPort();
	}
	
	@Override
	public void run() {
		try {
			while (!udpSocket.isClosed()) {
				udpSocket.receive(receivePacket);
				Server.getWorld().receiveUDPMessage(receivePacket.getData());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
