package ca.msbsoftware.accentis.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

	private static ThreadLocal<MessageDigest> shaDigests = new ThreadLocal<MessageDigest>() {
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("SHA-2"); //$NON-NLS-1$
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			}
		}
	}; 
	
	private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/*
	 * Robert Jenkins' hash function
	 * 
	 * TODO: Properly attribute this code to Robert Jenkins.
	 */
	public static int hash(int value) {
		value = (value + 0x7ed55d16) + (value << 12);
		value = (value ^ 0xc761c23c) ^ (value >> 19);
		value = (value + 0x165667b1) + (value << 5);
		value = (value + 0xd3a2646c) ^ (value << 9);
		value = (value + 0xfd7046c5) + (value << 3);
		value = (value ^ 0xb55a4f09) ^ (value >> 16);

		return value;
	}
	
	public static String digestIntoHex(char[] value) {
		MessageDigest digest = shaDigests.get();
		digest.reset();
		
		CharBuffer charBuffer = CharBuffer.wrap(value);
		ByteBuffer byteBuffer = Charset.defaultCharset().encode(charBuffer);
		
		return convertIntoHex(digest.digest(byteBuffer.array()));
	}
	
	private static String convertIntoHex(byte[] bytes) {
		StringBuilder buffer = new StringBuilder();
		
		for (byte b : bytes)
			buffer.append(HEX_CHARS[(b & 0xF0 >> 4)]).append(HEX_CHARS[(b & 0x0F)]);
		
		return buffer.toString();
	}
}
