package com.integrareti.integraframework.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * This class offers methods to manipulate strings
 * 
 * @author Thiago
 * 
 */
public class StringUtil {

		
	/**
	 * Removes an accent character of a string and replace it with character
	 * without accent. Example: João --> Joao
	 * 
	 * @param string
	 * @return
	 */
	public static String changeAccent(String string) {
		string = string.replaceAll("[èéêë]", "e");
		string = string.replaceAll("[ûùúü]", "u");
		string = string.replaceAll("[ïîíì]", "i");
		string = string.replaceAll("[àâáãä]", "a");
		string = string.replaceAll("[òóõôö]", "o");
		string = string.replaceAll("ç", "c");
		string = string.replaceAll("[ÈÉÊË]", "E");
		string = string.replaceAll("[ÛÙÚÜ]", "U");
		string = string.replaceAll("[ÏÎÍÌ]", "I");
		string = string.replaceAll("[ÀÂÁÃÄ]", "A");
		string = string.replaceAll("[ÒÓÕÔÖ]", "O");
		string = string.replaceAll("[Ç]", "C");
		return string;
	}

	/**
	 * Replaces a substrind of a string by another string
	 * 
	 * @param string
	 * @param out
	 * @param in
	 * @return
	 */
	public static String replaceSubstring(String string, String out, String in) {
		return string.replace(out, in);
	}

	/**
	 * Removes a point character of a string and replace it with a empty string.
	 * Example: a. --> a
	 * 
	 * @param string
	 * @return
	 */
	public static String removePoint(String string) {
		string = string.replaceAll("[.]", "");
		return string;
	}

	/**
	 * 
	 * @param string
	 * @return Returns true if a string can be a integer number. However false.
	 */
	public static boolean isInteger(String string){
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param string
	 * @return An encrypt string (MD5)
	 * @throws NoSuchAlgorithmException
	 */
	public static String MD5Encrypt(String string)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		BigInteger hash = null;
		hash = new BigInteger(1, md.digest(string.getBytes()));
		String s = hash.toString(16);
		if (s.length() % 2 != 0)
			s = "0" + s;
		return s;
	}

	/**
	 * 
	 * @param string
	 * @return An encrypt string (SHA-1)
	 * @throws NoSuchAlgorithmException
	 */
	public static String SHA1Encrypt(String string) {
		byte[] bytes = null;
		MessageDigest hash = null;
		try {
			bytes = string.getBytes("ISO-8859-1");
			hash = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		byte[] sha1Bytes = hash.digest(bytes);
		return encodeBase16(sha1Bytes);
	}

	/**
	 * Base64 encoding. Charset ISO-8859-1 is assumed.
	 */
	public static String encodeBase64(byte[] bytes)
			throws UnsupportedEncodingException {
		BASE64Encoder enc = new BASE64Encoder();
		return enc.encode(bytes);
	}

	/**
	 * Base16 encoding (HEX).
	 */
	protected static String encodeBase16(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			// top 4 bits
			char c = (char) ((b >> 4) & 0xf);
			if (c > 9)
				c = (char) ((c - 10) + 'a');
			else
				c = (char) (c + '0');
			sb.append(c);
			// bottom 4 bits
			c = (char) (b & 0xf);
			if (c > 9)
				c = (char) ((c - 10) + 'a');
			else
				c = (char) (c + '0');
			sb.append(c);
		}
		return sb.toString();
	}
	
	

}
