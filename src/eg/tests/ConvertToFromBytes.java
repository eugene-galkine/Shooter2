package eg.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import eg.utils.ByteArrayUtils;

public class ConvertToFromBytes {

	@Test
	public void testIntConversion() {
		int actual = 5;
		byte[] data = ByteArrayUtils.appendInt(new byte[4], 0, actual);
		assertEquals(ByteArrayUtils.parseInt(data, 0), actual);
	}
	
	@Test
	public void testFloatConversion() {
		float actual = 8.975f;
		byte[] data = ByteArrayUtils.appendFloat(new byte[4], 0, actual);
		assertTrue(ByteArrayUtils.parseFloat(data, 0) == actual);
	}

}
