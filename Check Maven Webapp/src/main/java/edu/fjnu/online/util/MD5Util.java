package edu.fjnu.online.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	public static String getData(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(str.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				String result = Integer.toHexString(data[i] & 0xff);
				String temp = null;
				if (result.length() == 1) {
					temp = "0" + result;
				} else {
					temp = result;
				}
				sb.append(temp);
			}
			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * Encodes a string 2 MD5
	 * 
	 * @param str String to encode
	 * @return Encoded String
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptData(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] hash = md.digest();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hexString.toString();
	}
	
	public static void main(String[] args) {
		System.out.println("e10adc3949ba59abbe56e057f20f883e".equals(getData("123456")));
		System.out.println(getData("123456"));
	}
}
