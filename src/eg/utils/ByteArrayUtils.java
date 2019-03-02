package eg.utils;

public class ByteArrayUtils {
	public static int parseInt(byte[] data, int pos) {
		//TODO check for errors
		int result = 0;
		result |= data[pos] << 24;
		result |= (data[pos + 1] & 0xff) << 16;
		result |= (data[pos + 2] & 0xff) << 8;
		result |= (data[pos + 3] & 0xff);
		
		return result;
	}
	
	public static float parseFloat(byte[] data, int pos) {
		//TODO check for errors
		return Float.intBitsToFloat(
				data[pos] << 24 |
				(data[pos + 1] & 0xff) << 16 |
				(data[pos + 2] & 0xff) << 8 |
				(data[pos + 3] & 0xff));
	}
	
	public static byte[] appendInt(byte[] data, int index, int msg) {	
		data[index] = (byte)(msg >> 0);
		data[index + 1] = (byte)(msg >> 8);
		data[index + 2] = (byte)(msg >> 16);
		data[index + 3] = (byte)(msg >> 24);
		
		return data;
	}
	
	public static byte[] appendFloat(byte[] data, int index, float msg) {	
		int converted = Float.floatToIntBits(msg);
		data[index] = (byte)(converted >> 0);
		data[index + 1] = (byte)(converted >> 8);
		data[index + 2] = (byte)(converted >> 16);
		data[index + 3] = (byte)(converted >> 24);
		
		return data;
	}
}