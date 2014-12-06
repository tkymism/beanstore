package com.tkym.labs.beanstore.hbase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import com.tkym.labs.beanmeta.Key;
import com.tkym.labs.beans.PersonMeta;

public class BytesTest {
	
	@Test
	public void testBytesCase001(){
		byte[] ret = Bytes.toBytes(false);
		assertThat(toHex(ret), is("00"));
	}
	@Test
	public void testBytesCase002(){
		byte[] ret = Bytes.toBytes(true);
		assertThat(toHex(ret), is("ff"));
	}
	@Test
	public void testBytesCase010(){
		byte[] ret = Bytes.toBytes(1);
		assertThat(toHex(ret), is("00000001"));
	}
	@Test
	public void testBytesCase011(){
		byte[] ret = Bytes.toBytes(-1);
		assertThat(toHex(ret), is("ffffffff"));
	}
	@Test
	public void testBytesCase012(){
		byte[] ret = Bytes.toBytes(Integer.MAX_VALUE);
		assertThat(toHex(ret), is("7fffffff"));
	}
	@Test
	public void testBytesCase013(){
		byte[] ret = Bytes.toBytes(Integer.MIN_VALUE);
		assertThat(toHex(ret), is("80000000"));
	}
	@Test
	public void testBytesCase021(){
		byte[] ret = Bytes.toBytes(1L);
		assertThat(toHex(ret), is("0000000000000001"));
	}
	@Test
	public void testBytesCase022(){
		byte[] ret = Bytes.toBytes(-1L);
		assertThat(toHex(ret), is("ffffffffffffffff"));
	}
	@Test
	public void testBytesCase023(){
		byte[] ret = Bytes.toBytes(Long.MAX_VALUE);
		assertThat(toHex(ret), is("7fffffffffffffff"));
	}
	@Test
	public void testBytesCase024(){
		byte[] ret = Bytes.toBytes(Long.MIN_VALUE);
		assertThat(toHex(ret), is("8000000000000000"));
	}
	@Test
	public void testBytesCase100(){
		assertThat(toHex(Bytes.toBytes("あああああ")), is("e38182e38182e38182e38182e38182"));
		assertThat(toHex(Bytes.toBytes("99999")), is("3939393939"));
		assertThat(toHex(Bytes.toBytes("aaaaa")), is("6161616161"));
	}
	private static final PersonMeta PERSON = PersonMeta.get();
	@Test
	public void testKeyConverterToBytesCase001(){
		assertThat(toHex(KeyConverter.toBytes(3)), is("00000003"));
	}
	@Test
	public void testKeyConverterToBytesCase002(){
		assertThat(toHex(KeyConverter.toBytes(3L)), is("0000000000000003"));
	}
	@Test
	public void testKeyConverterToBytesCase003(){
		assertThat(toHex(KeyConverter.toBytes("any")), is("616e79"));
	}
	@Test
	public void testKeyConverterToBytesCase004(){
		assertThat(toHex(KeyConverter.toBytes("髙")), is("e9ab99"));
	}
	
	static class KeyConverter{
		private final byte[] namespace;
		KeyConverter(){
			this(new byte[0]);
		}
		KeyConverter(byte[] namespace){
			this.namespace = namespace;
		}
		<BT,KT> byte[] encode(Key<BT,KT> key){
			byte[] head;
			if (key.getParent() != null) head = encode(key.getParent());
			else head = namespace;
			return Bytes.add(head, toBytes(key.value()));
		}
		static <KT> byte[] toBytes(KT val){
			if (val instanceof Long) 
				return Bytes.toBytes(((Long) val).longValue());
			else if (val instanceof Integer) 
				return Bytes.toBytes(((Integer) val).intValue());
			else if (val instanceof String)
				return Bytes.toBytes((String) val);
			else if (val instanceof Short)
				return Bytes.toBytes(((Short) val).shortValue());
			else if (val instanceof Byte)
				return Bytes.toBytes(((Byte) val).byteValue());
			else if (val instanceof Float)
				return Bytes.toBytes(((Float) val).floatValue());
			else if (val instanceof Double)
				return Bytes.toBytes(((Double) val).doubleValue());
			else if (val instanceof Boolean)
				return Bytes.toBytes(((Boolean) val).booleanValue());
			else if (val instanceof BigDecimal)
				return Bytes.toBytes((BigDecimal) val);
			else
				throw new IllegalArgumentException(
						"val is not support type"+val.getClass().getName());
		}
	}
	static String toHex(byte[] bytes){
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) sb.append(String.format("%02x", b));
		return sb.toString();
	}
}
