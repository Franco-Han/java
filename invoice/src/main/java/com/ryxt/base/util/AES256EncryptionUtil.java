package com.ryxt.base.util;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;

public class AES256EncryptionUtil {
    static{
        try{
            Security.addProvider(new BouncyCastleProvider());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static final String TAG = AES256EncryptionUtil.class.getSimpleName();
    public static final String ALGORITHM = "AES/ECB/PKCS7Padding";
    private static String mPassword = "5478125486324785";

//    public static void main(String[] args) throws Exception {
////        String hello = AES256EncryptionUtil.encrypt("hello");
//        String word = AES256EncryptionUtil.decrypt("ebb7c703e675db3da397038b4c17823c");
//        SystemInfo.out.println(word);
//    }
    /**
     * 一次性设置password，后面无需再次设置
     * @param password
     */
    public static void setPassword(String password){
        mPassword = password;
    }

    /**
     * 生成key
     * @param password
     * @return
     * @throws Exception
     */
    private static byte[] getKeyByte(String password) throws Exception {
        byte[] seed = new byte[24];
        if(password != null) {
            seed = password.getBytes();
        }
        return seed;
    }

    /**
     * 加密
     * @param data
     * @return
     */
    public static String encrypt(String data, String password) throws Exception{
        String string = "";
        byte[] keyByte = getKeyByte(StringUtils.isNotEmpty(password) ? password : mPassword);
        SecretKeySpec keySpec = new SecretKeySpec(keyByte,"AES"); //生成加密解密需要的Key
        byte[] byteContent = data.getBytes("utf-8");
        Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] result = cipher.doFinal(byteContent);
//        string = Base64.encodeBase64String(result);  //转成String
        string = Base64.getEncoder().encodeToString(result);  //转成String

        return string;
    }

    /**
     * 解密
     * @param data
     * @return
     */
    public static String decrypt(String data, String password) {
        try {
            String string = "";
            byte[] keyByte = getKeyByte(StringUtils.isNotEmpty(password) ? password : mPassword);
//            byte[] byteContent = Base64.decodeBase64(data);//parseHexStr2Byte();  //转成byte
            byte[] byteContent = Base64.getDecoder().decode(data);
            Cipher cipher = Cipher.getInstance(ALGORITHM, "BC");
            SecretKeySpec keySpec = new SecretKeySpec(keyByte,"AES"); //生成加密解密需要的Key
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = cipher.doFinal(byteContent);
            string = new String(decoded);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 转化为String
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
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
     * 将16进制转换为二进制
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
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

    public static void main(String[] args) throws Exception {
//        String hello = AES256EncryptionUtil.encrypt("ML", CommonUtils.PASSWORD);
//        SystemInfo.out.println(hello);
//        String hello1 = AES256EncryptionUtil.encrypt("MR", CommonUtils.PASSWORD);
//        SystemInfo.out.println(hello1);
    }
}
