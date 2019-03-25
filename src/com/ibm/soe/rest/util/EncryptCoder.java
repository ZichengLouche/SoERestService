package com.ibm.soe.rest.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;

import javax.crypto.SecretKey;

import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.DESKeySpec;

public class EncryptCoder {

	private final static String DES = "DES";

	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {

		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		return cipher.doFinal(src);
	}

	/**
	 * 
	 * @param pass
	 *           
	 * @param key
	 *            
	 * @return
	 */
	public final static String encrypt(String password, String key) {
		try {
			return byte2String(encrypt(password.getBytes(), key.getBytes()));
		} catch (Exception e) {
		}
		return null;
	}

	public static String byte2String(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

}