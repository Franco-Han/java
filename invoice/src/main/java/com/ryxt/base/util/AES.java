package com.ryxt.base.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class AES {

	private static final String ALGORITHM = "AES";
	private static final int KEY_SIZE = 128;
	private static final int CACHE_SIZE = 1024;

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes());
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param password
	 *            解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {

		try {
			//在这里创建random并放入密码bytes
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			//移出方法到上面，linux下面会导致解密错误
			kgen.init(KEY_SIZE, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * 文件加密
	 * </p>
	 * 
	 * @param key
	 * @param sourceFilePath
	 * @param destFilePath
	 * @throws Exception
	 */
	public static void encryptFile(String key, String sourceFilePath, String destFilePath) throws Exception {
		File sourceFile = new File(sourceFilePath);
		File destFile = new File(destFilePath);
		if (sourceFile.exists() && sourceFile.isFile()) {
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			destFile.createNewFile();
			
			InputStream in = null;
			OutputStream out = null;
			CipherInputStream cin = null;
			try{
				in = new FileInputStream(sourceFile);
				out = new FileOutputStream(destFile);
				KeyGenerator kgen = KeyGenerator.getInstance("AES");
				// 防止linux下 随机生成key
				SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
				secureRandom.setSeed(key.getBytes());
				kgen.init(KEY_SIZE, secureRandom);
				SecretKey secretKey = kgen.generateKey();
				byte[] raw = secretKey.getEncoded();
				SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
				Cipher cipher = Cipher.getInstance(ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);// 初始化
				cin = new CipherInputStream(in, cipher);
				byte[] cache = new byte[CACHE_SIZE];
				int nRead = 0;
				while ((nRead = cin.read(cache)) != -1) {
					out.write(cache, 0, nRead);
					out.flush();
				}
			}finally{
				if(cin != null)
					cin.close();
				if(in != null)
					in.close();
				if(out != null)
					out.close();
			}
		}
	}

	/**
	 * <p>
	 * 文件解密
	 * </p>
	 * 
	 * @param key
	 * @param sourceFilePath
	 * @param destFilePath
	 * @throws Exception
	 */
	public static void decryptFile(String key, String sourceFilePath, String destFilePath) throws Exception {
		File sourceFile = new File(sourceFilePath);
		File destFile = new File(destFilePath);
		if (sourceFile.exists() && sourceFile.isFile()) {
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			destFile.createNewFile();
			
			FileInputStream in = null;
			FileOutputStream out = null;
			CipherOutputStream cout = null;
			try {
				in = new FileInputStream(sourceFile);
				out = new FileOutputStream(destFile);
				KeyGenerator kgen = KeyGenerator.getInstance("AES");
				// 防止linux下 随机生成key
				SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
				secureRandom.setSeed(key.getBytes());
				kgen.init(KEY_SIZE, secureRandom);
				SecretKey secretKey = kgen.generateKey();
				byte[] raw = secretKey.getEncoded();
				
				SecretKeySpec secretKeySpec = new SecretKeySpec(raw, ALGORITHM);
				Cipher cipher = Cipher.getInstance(ALGORITHM);
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
				
				cout = new CipherOutputStream(out, cipher);
				byte[] cache = new byte[CACHE_SIZE];
				int nRead = 0;
				while ((nRead = in.read(cache)) != -1) {
					cout.write(cache, 0, nRead);
					cout.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(cout != null){
					cout.close();
				}
				if(out != null){
					out.close();
				}
				if(in != null){
					in.close();
				}
			}
					
		}
	}

	/**
	 * <p>
	 * 转换密钥
	 * </p>
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	/*private static Key toKey(byte[] key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
		return secretKey;
	}*/

	/*@Test
	public void test() {
		GUID myGUID = new GUID(true);
		SystemInfo.out.println(myGUID.toString());

		String content = "test";
		String password = myGUID.toString();
		// String password = "12345678";
		// 加密
		SystemInfo.out.println("加密前：" + content);
		SystemInfo.out.println("key：" + password);
		byte[] encryptResult = encrypt(content, password);
		String encryptResultStr = parseByte2HexStr(encryptResult);
		SystemInfo.out.println("加密后：" + encryptResultStr);
		// 解密
		byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);
		byte[] decryptResult = decrypt(decryptFrom, password);
		SystemInfo.out.println("解密后：" + new String(decryptResult));
	}*/

}
