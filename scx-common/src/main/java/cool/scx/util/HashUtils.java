package cool.scx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.function.Supplier;
import java.util.zip.CRC32;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.MessageDigest.getInstance;
import static java.util.Objects.requireNonNull;

/**
 * HASH 工具类
 *
 * @author scx567888
 * @version 3.0.0
 */
public final class HashUtils {

    /**
     * 此缓冲区大小在内存和速度的综合测试中表现最优
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 64;

    private static final HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();

    public static String hash(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        requireNonNull(data, "Data must not be empty !!!");
        return HEX_FORMAT.formatHex(getInstance(algorithm).digest(data));
    }

    public static String hash(String data, String algorithm) throws NoSuchAlgorithmException {
        requireNonNull(data, "Data must not be empty !!!");
        return HEX_FORMAT.formatHex(getInstance(algorithm).digest(data.getBytes(UTF_8)));
    }

    public static String hash(File data, String algorithm) throws IOException, NoSuchAlgorithmException {
        requireNonNull(data, "Data must not be empty !!!");
        var messageDigest = getInstance(algorithm);
        var buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        try (var inputStream = new FileInputStream(data)) {
            while ((read = inputStream.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, read);
            }
        }
        return HEX_FORMAT.formatHex(messageDigest.digest());
    }

    /**
     * 此方法假定 指定算法一定存在 所以不向外显式抛出 {@link NoSuchAlgorithmException} 异常
     *
     * @param data      data
     * @param algorithm algorithm
     * @return hash
     */
    private static String hash0(byte[] data, String algorithm) {
        try {
            return hash(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法假定 指定算法一定存在 所以不向外显式抛出 {@link NoSuchAlgorithmException} 异常
     *
     * @param data      data
     * @param algorithm algorithm
     * @return hash
     */
    private static String hash0(String data, String algorithm) {
        try {
            return hash(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 此方法假定 指定算法一定存在 所以不向外显式抛出 {@link NoSuchAlgorithmException} 异常
     *
     * @param data      data
     * @param algorithm algorithm
     * @return hash
     */
    private static String hash0(File data, String algorithm) throws IOException {
        try {
            return hash(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha1(byte[] data) {
        return hash0(data, "SHA-1");
    }

    public static String sha1(String data) {
        return hash0(data, "SHA-1");
    }

    public static String sha1(File data) throws IOException {
        return hash0(data, "SHA-1");
    }

    public static String sha256(byte[] data) {
        return hash0(data, "SHA-256");
    }

    public static String sha256(String data) {
        return hash0(data, "SHA-256");
    }

    public static String sha256(File data) throws IOException {
        return hash0(data, "SHA-256");
    }

    public static String sha384(byte[] data) {
        return hash0(data, "SHA-384");
    }

    public static String sha384(String data) {
        return hash0(data, "SHA-384");
    }

    public static String sha384(File data) throws IOException {
        return hash0(data, "SHA-384");
    }

    public static String sha512(byte[] data) {
        return hash0(data, "SHA-512");
    }

    public static String sha512(String data) {
        return hash0(data, "SHA-512");
    }

    public static String sha512(File data) throws IOException {
        return hash0(data, "SHA-512");
    }

    public static String md5(byte[] data) {
        return hash0(data, "MD5");
    }

    public static String md5(String data) {
        return hash0(data, "MD5");
    }

    public static String md5(File data) throws IOException {
        return hash0(data, "MD5");
    }

    public static String hash(byte[] data, Supplier<Checksum> checksumSupplier) {
        requireNonNull(data, "Data must not be empty !!!");
        var checksum = checksumSupplier.get();
        checksum.update(data);
        return HEX_FORMAT.toHexDigits((int) checksum.getValue());
    }

    public static String hash(String data, Supplier<Checksum> checksumSupplier) {
        requireNonNull(data, "Data must not be empty !!!");
        var checksum = checksumSupplier.get();
        checksum.update(data.getBytes(UTF_8));
        return HEX_FORMAT.toHexDigits((int) checksum.getValue());
    }

    public static String hash(File data, Supplier<Checksum> checksumSupplier) throws IOException {
        requireNonNull(data, "Data must not be empty !!!");
        var checksum = checksumSupplier.get();
        var buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read;
        try (var inputStream = new FileInputStream(data)) {
            while ((read = inputStream.read(buffer)) != -1) {
                checksum.update(buffer, 0, read);
            }
        }
        return HEX_FORMAT.toHexDigits((int) checksum.getValue());
    }

    public static String crc32(String data) {
        return hash(data, CRC32::new);
    }

    public static String crc32(byte[] data) {
        return hash(data, CRC32::new);
    }

    public static String crc32(File data) throws IOException {
        return hash(data, CRC32::new);
    }

    public static String crc32c(String data) {
        return hash(data, CRC32C::new);
    }

    public static String crc32c(byte[] data) {
        return hash(data, CRC32C::new);
    }

    public static String crc32c(File data) throws IOException {
        return hash(data, CRC32C::new);
    }

}
