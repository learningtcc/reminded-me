package com.homefellas.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class CryptoHelper {
	private static Cipher cipherEncrypt = null;           
	private static Cipher cipherDecrypt = null;           

	static{
		try {
			// create secretkey, DO NOT CHANGE THIS LINE OF CODE EVER or pw will not be accessible.           
			SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec("keyname_hfcrypto".getBytes()));  
			// create ciphers       
			cipherEncrypt = Cipher.getInstance("DES");    
			cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKey);   
			
			cipherDecrypt = Cipher.getInstance("DES");           
			cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKey);    
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

/**
 * ENCRYPT:  Must send two parameters, key and value.  Key being
 * key used to access encrypted value, value being one to be encrypted/decrypted.
 */
//	public static String encrypt(String value) throws Exception{
//			// encrypt 
//			return value = new BASE64Encoder().encode(cipherEncrypt.doFinal(value.getBytes("UTF8")));
//	}
/**
 * DECRYPT:  Must send two parameters, key and value.  Key being
 * key used to access encrypted value, value being one to be encrypted/decrypted.
 */	
//	public static String decrypt(String value) throws Exception{
//			//decrypt the value
//			return value = new String(cipherDecrypt.doFinal(new BASE64Decoder().decodeBuffer(value)), "UTF8");
//	}
	
}
