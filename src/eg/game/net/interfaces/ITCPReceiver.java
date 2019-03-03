package eg.game.net.interfaces;

public interface ITCPReceiver {
    void onNewTCPMessage(byte[] data);
}
