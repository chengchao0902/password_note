package vip.chengchao.tools.mypwnode.cipher;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String AES = "AES";
    private final static String HEX = "0123456789ABCDEF";
    private final static int JELLY_BEAN_4_2 = 17;

    /**
     * 加密
     *
     * @param key 密钥
     * @param src 加密文本
     * @return
     * @throws Exception
     */
    public static String encrypt(String key, String src) {
        byte[] rawKey;
        try {
            rawKey = getRawKey(key);
            byte[] result = encrypt(rawKey, src.getBytes());
            return toHex(result);
        } catch (Exception e) {
            Log.e(AES, "encrypt error,key:" + key + ",src=" + src);
            return src;
        }
    }

    /**
     * 解密
     *
     * @param key       密钥
     * @param encrypted 待解密文本
     * @return
     * @throws Exception
     */
    public static String decrypt(String key, String encrypted) {
        byte[] rawKey;
        try {
            rawKey = getRawKey(key);
            byte[] enc = toByte(encrypted);
            byte[] result = decrypt(rawKey, enc);
            return new String(result);
        } catch (Exception e) {
            Log.e(AES, "decrypt error,key=" + key + ",encrypted=" + encrypted);
            return encrypted;
        }

    }

    /**
     * 获取256位的加密密钥
     *
     * @param seed
     * @return
     * @throws Exception
     */
    private static byte[] getRawKey(String seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed.getBytes("UTF-8"));
        // 256 bits or 128 bits,192bits
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    /**
     * 真正的加密过程
     *
     * @param key
     * @param src
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    /**
     * 真正的解密过程
     *
     * @param key
     * @param encrypted
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] key, byte[] encrypted)
            throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private static Cipher getCipher(int opmode, byte[] key) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(opmode, skeySpec);
        return cipher;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}