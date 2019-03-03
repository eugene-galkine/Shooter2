package eg.server.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import eg.utils.GlobalConstants;

public class UDPConnection {
	private DatagramPacket udpPacket;
	private byte[] sendData;
	private DatagramSocket clientSocket;
	
	public UDPConnection(InetAddress tcpAddress, int tcpPort) throws SocketException {
		this.sendData = new byte[GlobalConstants.UDP_PACKET_SIZE];
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
	    	udpPacket.setData(data);//TODO move to UDPConnection
			clientSocket.send(udpPacket);
		} catch (IOException e) 
	    {
			e.printStackTrace();
		}
	}
}
