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
	public void testIntConversionMin() {
		int actual = Integer.MIN_VALUE;
		byte[] data = ByteArrayUtils.appendInt(new byte[4], 0, actual);
		assertEquals(ByteArrayUtils.parseInt(data, 0), actual);
	}
	
	@Test
	public void testIntConversionMax() {
		int actual = Integer.MAX_VALUE;
		byte[] data = ByteArrayUtils.appendInt(new byte[4], 0, actual);
		assertEquals(ByteArrayUtils.parseInt(data, 0), actual);
	}
	
	@Test
	public void testFloatConversion() {
		float actual = 8.975f;
		byte[] data = ByteArrayUtils.appendFloat(new byte[4], 0, actual);
		assertTrue(ByteArrayUtils.parseFloat(data, 0) == actual);
	}
	
	@Test
	public void testFloatConversionMin() {
		float actual = Float.MIN_VALUE;
		byte[] data = ByteArrayUtils.appendFloat(new byte[4], 0, actual);
		assertTrue(ByteArrayUtils.parseFloat(data, 0) == actual);
	}

	@Test
	public void testFloatConversionMax() {
		float actual = Float.MAX_VALUE;
		byte[] data = ByteArrayUtils.appendFloat(new byte[4], 0, actual);
		assertTrue(ByteArrayUtils.parseFloat(data, 0) == actual);
	}
	
	@Test
	public void testFloatConversionInfinity() {
		float actual = Float.POSITIVE_INFINITY;
		byte[] data = ByteArrayUtils.appendFloat(new byte[4], 0, actual);
		assertTrue(ByteArrayUtils.parseFloat(data, 0) == actual);
	}
	
	@Test
	public void testFloatConversionNegInfinity() {
		float actual = Float.NEGATIVE_INFINITY;
		byte[] data = ByteArrayUtils.appendFloat(new byte[4], 0, actual);
		assertTrue(ByteArrayUtils.parseFloat(data, 0) == actual);
	}
	
	@Test
	public void testFloatConversionSmallNeg() {
		float actual = -0.143673f;
		byte[] data = ByteArrayUtils.appendFloat(new byte[4], 0, actual);
		assertTrue(ByteArrayUtils.parseFloat(data, 0) == actual);
	}
}
