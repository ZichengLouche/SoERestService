package com.ibm.soe.rest.util;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DescryptCoder {
	private final static String DES = "DES";
	
	/**
	 * @param src
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

		return cipher.doFinal(src);
	}

	public final static String decrypt(String data, String key) {
		try {
			return new String(decrypt(String2byte(data.getBytes()), key.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] String2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("The length is not an even number");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

}