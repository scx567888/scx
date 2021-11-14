package cool.scx.util;

import org.jasypt.util.binary.AES256BinaryEncryptor;
import org.jasypt.util.binary.BinaryEncryptor;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jasypt.util.text.TextEncryptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 加密,解密工具类 <br>
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class CryptoUtils {

    /**
     * 密码 加密器
     */
    private static final PasswordEncryptor defaultPasswordEncryptor = new BasicPasswordEncryptor();

    /**
     * <p>getTextEncryptor.</p>
     *
     * @param encryptorPassword 加密解密密钥
     * @return 加密解密工具类对象
     */
    private static TextEncryptor getTextEncryptor(String encryptorPassword) {
        var encryptor = new AES256TextEncryptor();
        encryptor.setPassword(encryptorPassword);
        return encryptor;
    }

    /**
     * aaa
     *
     * @param encryptorPassword a
     * @return a
     */
    private static BinaryEncryptor getBinaryEncryptor(String encryptorPassword) {
        var encryptor = new AES256BinaryEncryptor();
        encryptor.setPassword(encryptorPassword);
        return encryptor;
    }

    /**
     * 使用自定义的密码 , 加密字符串
     *
     * @param text              待加密的字符串
     * @param encryptorPassword 自定义的密码
     * @return a 密文
     */
    public static String encryptText(String text, String encryptorPassword) {
        return getTextEncryptor(encryptorPassword).encrypt(text);
    }

    /**
     * 使用自定义的密码 , 解密字符串
     *
     * @param text              密文
     * @param encryptorPassword 自定义的密码
     * @return a 结果
     */
    public static String decryptText(String text, String encryptorPassword) {
        return getTextEncryptor(encryptorPassword).decrypt(text);
    }

    /**
     * 使用自定义的密码 , 加密
     *
     * @param binary            待加密的字符串
     * @param encryptorPassword 自定义的密码
     * @return a 密文
     */
    public static byte[] encryptBinary(byte[] binary, String encryptorPassword) {
        return getBinaryEncryptor(encryptorPassword).encrypt(binary);
    }

    /**
     * 使用自定义的密码 , 解密
     *
     * @param encryptedBinary   密文
     * @param encryptorPassword 自定义的密码
     * @return a 结果
     */
    public static byte[] decryptBinary(byte[] encryptedBinary, String encryptorPassword) {
        return getBinaryEncryptor(encryptorPassword).decrypt(encryptedBinary);
    }

    /**
     * 根据 字符串 获取(加密) BASE64
     *
     * @param str a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String encodeBase64(String str) {
        var encoder = Base64.getEncoder();
        return encoder.encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 根据 BASE64 获取(解密) 字符串
     *
     * @param base64 a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String decodeBase64(String base64) {
        var decoder = Base64.getDecoder();
        return new String(decoder.decode(base64), StandardCharsets.UTF_8);
    }

    /**
     * 加密密码
     *
     * @param password p
     * @return p
     */
    public static String encryptPassword(String password) {
        return defaultPasswordEncryptor.encryptPassword(password);
    }

    /**
     * 校验密码
     *
     * @param plainPassword     原密码
     * @param encryptedPassword 加密后的密码
     * @return a
     */
    public static boolean checkPassword(String plainPassword, String encryptedPassword) {
        return defaultPasswordEncryptor.checkPassword(plainPassword, encryptedPassword);
    }

}
