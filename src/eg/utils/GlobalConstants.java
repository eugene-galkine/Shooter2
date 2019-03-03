package eg.utils;

public class GlobalConstants {
	public static final int UDP_PACKET_SIZE = 24;
	public static final int TCP_PACKET_SIZE = 128;
	
	public static final byte UDP_CMD_POSITION = -1;
	
	public static final byte TCP_CMD_SHOOT = 0;
	public static final byte TCP_CMD_GRENADE = 1;
	public static final byte TCP_CMD_NEW_PLAYER = 2;
	public static final byte TCP_CMD_REMOVE_PLAYER = 3;
	public static final byte TCP_CMD_SPAWN = 4;
	public static final byte TCP_CMD_REJECTED = 5;
	public static final byte TCP_CMD_CONNECTED = 6;
	public static final byte TCP_CMD_HIT = 7;
	public static final byte TCP_CMD_DEAD = 8;
}
