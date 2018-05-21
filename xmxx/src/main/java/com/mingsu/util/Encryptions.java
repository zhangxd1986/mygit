package com.mingsu.util;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class Encryptions {
	
	public static final String KEY_SHA = "SHA";
	
	/**
	 * 加密密码
	 * 
	 * @param pwd
	 * @return
	 */
	public static String encSHAPasswd ( String pwd ) {
		String encPwd = null;
		try {
			encPwd = new String ( Base64.encodeBase64 ( (_encryptSHA ( pwd.getBytes ())))).toUpperCase ();
		} catch ( Exception e ) {
			return null;
		}
		
		return encPwd;
	}
	
	/**
	 * SHA加密
	 * 
	 * @param data
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	private static byte[] _encryptSHA ( byte[] data ) throws Exception {
		MessageDigest sha = MessageDigest.getInstance ( KEY_SHA);
		sha.update ( data);
		return sha.digest ();
	}
	
	public static void main ( String[] args ) {
		System.out.println(encSHAPasswd("123456"));
	}
}
