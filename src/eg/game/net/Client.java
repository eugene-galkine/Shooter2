package eg.game.net;

import java.io.IOException;

import eg.game.net.interfaces.ITCPReceiver;
import eg.game.net.interfaces.IUDPReceiver;

public class Client implements IUDPReceiver, ITCPReceiver {
    private final ClientProxy clientProxy;
    private final String ip;
    private final int port;
    private UDPClient udpClient;
    private TCPClient tcpClient;
    
    //cannot access outside of package
	Client (String ip, int port) {
    	clientProxy = new ClientProxy(this);
    	this.ip = ip;
    	this.port = port;
	}
	
	void connect() {
		try {
			tcpClient = new TCPClient(this, ip, port);
			udpClient = new UDPClient(this, ip, tcpClient.getLocalPort() + 1, port + 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	ClientProxy getProxy() {
		return clientProxy;
	}

	void sendUDPMessage(byte[] data) {
		udpClient.sendPacket(data);
	}

	void sendTCPMessage(byte[] data) {
		tcpClient.sendPacket(data);
	}

	@Override
	public void onNewUDPMessage(byte[] data, int length) {
		clientProxy.receivedUDPMessage(data, length);
	}

	@Override
	public void onNewTCPMessage(byte[] data) {
		clientProxy.receivedTCPMessage(data);
	}
}
