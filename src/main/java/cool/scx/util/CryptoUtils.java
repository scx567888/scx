package cool.scx.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.util.binary.AES256BinaryEncryptor;
import org.jasypt.util.binary.BinaryEncryptor;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jasypt.util.text.TextEncryptor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

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

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

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

    /**
     * 加密
     *
     * @param algorithm 算法
     * @param password  密码
     * @param data      数据
     * @return 加密后的数据
     * @throws NoSuchAlgorithmException  a
     * @throws NoSuchProviderException   a
     * @throws InvalidKeySpecException   a
     * @throws NoSuchPaddingException    a
     * @throws InvalidKeyException       a
     * @throws IllegalBlockSizeException a
     * @throws BadPaddingException       a
     */
    public static byte[] encrypt(String algorithm, byte[] password, byte[] data) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        var key = new SecretKeySpec(password, algorithm);
        var cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param algorithm 算法
     * @param password  密码
     * @param data      密文
     * @return 解密后的数据
     * @throws NoSuchPaddingException    a
     * @throws NoSuchAlgorithmException  a
     * @throws NoSuchProviderException   a
     * @throws InvalidKeyException       a
     * @throws IllegalBlockSizeException a
     * @throws BadPaddingException       a
     */
    public static byte[] decrypt(String algorithm, byte[] password, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        var key = new SecretKeySpec(password, algorithm);
        var cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

}
