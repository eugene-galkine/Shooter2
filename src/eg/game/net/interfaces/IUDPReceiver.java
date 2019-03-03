package eg.game.net.interfaces;

public interface IUDPReceiver {
    void onNewUDPMessage(byte[] data, int length);
}
