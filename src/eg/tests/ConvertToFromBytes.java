package eg.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import eg.utils.ByteArrayUtils;

public class ConvertToFromBytes {

	@Test
	public void testIntConversion() {
		int actual = 5;
		byte[] data = new byte[4];
		ByteArrayUtils.appendInt(data, 0, actual);
		assertEquals(ByteArrayUtils.parseInt(data, 0), actual);
	}
	
	@Test
	public void testIntConversionMin() {
		int actual = Integer.MIN_VALUE;
		byte[] data = new byte[4];
		ByteArrayUtils.appendInt(data, 0, actual);
		assertEquals(ByteArrayUtils.parseInt(data, 0), actual);
	}
	
	@Test
	public void testIntConversionMax() {
		int actual = Integer.MAX_VALUE;
		byte[] data = new byte[4];
		ByteArrayUtils.appendInt(data, 0, actual);
		assertEquals(ByteArrayUtils.parseInt(data, 0), actual);
	}
	
	@Test
	public void testFloatConversion() {
		float actual = 8.975f;
		byte[] data = new byte[4];
		ByteArrayUtils.appendFloat(data, 0, actual);
		assertEquals(ByteArrayUtils.parseFloat(data, 0), actual, 0.0);
	}
	
	@Test
	public void testFloatConversionMin() {
		float actual = Float.MIN_VALUE;
		byte[] data = new byte[4];
		ByteArrayUtils.appendFloat(data, 0, actual);
		assertEquals(ByteArrayUtils.parseFloat(data, 0), actual, 0.0);
	}

	@Test
	public void testFloatConversionMax() {
		float actual = Float.MAX_VALUE;
		byte[] data = new byte[4];
		ByteArrayUtils.appendFloat(data, 0, actual);
		assertEquals(ByteArrayUtils.parseFloat(data, 0), actual, 0.0);
	}
	
	@Test
	public void testFloatConversionInfinity() {
		float actual = Float.POSITIVE_INFINITY;
		byte[] data = new byte[4];
		ByteArrayUtils.appendFloat(data, 0, actual);
		assertEquals(ByteArrayUtils.parseFloat(data, 0), actual, 0.0);
	}
	
	@Test
	public void testFloatConversionNegInfinity() {
		float actual = Float.NEGATIVE_INFINITY;
		byte[] data = new byte[4];
		ByteArrayUtils.appendFloat(data, 0, actual);
		assertEquals(ByteArrayUtils.parseFloat(data, 0), actual, 0.0);
	}
	
	@Test
	public void testFloatConversionSmallNeg() {
		float actual = -0.143673f;
		byte[] data = new byte[4];
		ByteArrayUtils.appendFloat(data, 0, actual);
		assertEquals(ByteArrayUtils.parseFloat(data, 0), actual, 0.0);
	}
}
