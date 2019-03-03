package eg.server.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import eg.utils.GlobalConstants;

public class UDPConnection {
	private DatagramPacket udpPacket;
	private DatagramSocket clientSocket;
	
	UDPConnection(InetAddress tcpAddress, int tcpPort) throws SocketException {
		byte[] sendData = new byte[GlobalConstants.UDP_PACKET_SIZE];
		clientSocket = new DatagramSocket();
		udpPacket = new DatagramPacket(sendData, sendData.length, tcpAddress, tcpPort+1);
	}
	
	public void close() {
		clientSocket.close();
	}

	public void setPacket(byte[] data) {
		//send a message over udp
	    try 
	    {
	    	udpPacket.setData(data);
			clientSocket.send(udpPacket);
		} catch (IOException e) 
	    {
			e.printStackTrace();
		}
	}
}
