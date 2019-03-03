package eg.game.net;

import eg.game.net.interfaces.ITCPReceiver;
import eg.utils.GlobalConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class TCPClient extends Thread {
    private Socket socket;
    private OutputStream outputStream;
    private final ITCPReceiver client;

    TCPClient(ITCPReceiver client, String ip, int port) throws IOException {
        this.client = client;

        socket = new Socket(ip, port);
        socket.setTcpNoDelay(true);
        outputStream = socket.getOutputStream();

        start();
    }

    @Override
    public void run() {
        InputStream inFromServer;

        try {
            inFromServer = socket.getInputStream();

            //loop to reciece tcp messages
            byte[] data = new byte[GlobalConstants.TCP_PACKET_SIZE];
            while (!socket.isClosed() && inFromServer.read(data) != -1)
                client.onNewTCPMessage(data);

            inFromServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int getLocalPort() {
        return socket.getLocalPort();
    }

    void sendPacket(byte[] data) {
        //send a message over tcp
        try
        {
            byte[] buffer = new byte[GlobalConstants.TCP_PACKET_SIZE];
            for (int i = 0; i < data.length && i < buffer.length; i++)
                buffer[i] = data[i];
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
