package cool.scx.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.MessageDigest;
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
 * HASH 工具类 (注意 !!! 返回的十六进制均为大写格式)
 *
 * @author scx567888
 * @version 2.4.5
 */
public final class HashUtils {

    /**
     * 此缓冲区大小在内存和速度的综合测试中表现最优
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 64;

    private static final HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();

    public static String formatHex(byte[] bytes) {
        return HEX_FORMAT.formatHex(bytes);
    }

    public static byte[] hash(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        requireNonNull(data, "Data must not be empty !!!");
        return updateDigest(getInstance(algorithm), data).digest();
    }

    public static byte[] hash(String data, String algorithm) throws NoSuchAlgorithmException {
        requireNonNull(data, "Data must not be empty !!!");
        return updateDigest(getInstance(algorithm), data).digest();
    }

    public static byte[] hash(InputStream data, String algorithm) throws IOException, NoSuchAlgorithmException {
        requireNonNull(data, "Data must not be empty !!!");
        return updateDigest(getInstance(algorithm), data).digest();
    }

    public static byte[] hash(File data, String algorithm) throws IOException, NoSuchAlgorithmException {
        requireNonNull(data, "Data must not be empty !!!");
        return updateDigest(getInstance(algorithm), data).digest();
    }

    public static byte[] hash(Path data, String algorithm, OpenOption... options) throws IOException, NoSuchAlgorithmException {
        requireNonNull(data, "Data must not be empty !!!");
        return updateDigest(getInstance(algorithm), data, options).digest();
    }

    public static long hash(byte[] data, Supplier<Checksum> checksumSupplier) {
        requireNonNull(data, "Data must not be empty !!!");
        return updateChecksum(checksumSupplier.get(), data).getValue();
    }

    public static long hash(String data, Supplier<Checksum> checksumSupplier) {
        requireNonNull(data, "Data must not be empty !!!");
        return updateChecksum(checksumSupplier.get(), data).getValue();
    }

    public static long hash(InputStream data, Supplier<Checksum> checksumSupplier) throws IOException {
        requireNonNull(data, "Data must not be empty !!!");
        return updateChecksum(checksumSupplier.get(), data).getValue();
    }

    public static long hash(File data, Supplier<Checksum> checksumSupplier) throws IOException {
        requireNonNull(data, "Data must not be empty !!!");
        return updateChecksum(checksumSupplier.get(), data).getValue();
    }

    public static long hash(Path data, Supplier<Checksum> checksumSupplier, OpenOption... options) throws IOException {
        requireNonNull(data, "Data must not be empty !!!");
        return updateChecksum(checksumSupplier.get(), data, options).getValue();
    }

    public static String hashAsHex(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        return HEX_FORMAT.formatHex(hash(data, algorithm));
    }

    public static String hashAsHex(String data, String algorithm) throws NoSuchAlgorithmException {
        return HEX_FORMAT.formatHex(hash(data, algorithm));
    }

    public static String hashAsHex(InputStream data, String algorithm) throws IOException, NoSuchAlgorithmException {
        return HEX_FORMAT.formatHex(hash(data, algorithm));
    }

    public static String hashAsHex(File data, String algorithm) throws IOException, NoSuchAlgorithmException {
        return HEX_FORMAT.formatHex(hash(data, algorithm));
    }

    public static String hashAsHex(Path data, String algorithm, OpenOption... options) throws IOException, NoSuchAlgorithmException {
        return HEX_FORMAT.formatHex(hash(data, algorithm, options));
    }

    public static String hashAsHex(byte[] data, Supplier<Checksum> checksumSupplier) {
        return HEX_FORMAT.toHexDigits((int) hash(data, checksumSupplier));
    }

    public static String hashAsHex(String data, Supplier<Checksum> checksumSupplier) {
        return HEX_FORMAT.toHexDigits((int) hash(data, checksumSupplier));
    }

    public static String hashAsHex(InputStream data, Supplier<Checksum> checksumSupplier) throws IOException {
        return HEX_FORMAT.toHexDigits((int) hash(data, checksumSupplier));
    }

    public static String hashAsHex(File data, Supplier<Checksum> checksumSupplier) throws IOException {
        return HEX_FORMAT.toHexDigits((int) hash(data, checksumSupplier));
    }

    public static String hashAsHex(Path data, Supplier<Checksum> checksumSupplier, OpenOption... options) throws IOException {
        return HEX_FORMAT.toHexDigits((int) hash(data, checksumSupplier, options));
    }

    public static byte[] sha1(byte[] data) {
        return hash0(data, "SHA-1");
    }

    public static byte[] sha1(String data) {
        return hash0(data, "SHA-1");
    }

    public static byte[] sha1(InputStream data) throws IOException {
        return hash0(data, "SHA-1");
    }

    public static byte[] sha1(File data) throws IOException {
        return hash0(data, "SHA-1");
    }

    public static byte[] sha1(Path data, OpenOption... options) throws IOException {
        return hash0(data, "SHA-1", options);
    }

    public static String sha1Hex(byte[] data) {
        return hashAsHex0(data, "SHA-1");
    }

    public static String sha1Hex(String data) {
        return hashAsHex0(data, "SHA-1");
    }

    public static String sha1Hex(InputStream data) throws IOException {
        return hashAsHex0(data, "SHA-1");
    }

    public static String sha1Hex(File data) throws IOException {
        return hashAsHex0(data, "SHA-1");
    }

    public static String sha1Hex(Path data, OpenOption... options) throws IOException {
        return hashAsHex0(data, "SHA-1", options);
    }

    public static byte[] sha256(byte[] data) {
        return hash0(data, "SHA-256");
    }

    public static byte[] sha256(String data) {
        return hash0(data, "SHA-256");
    }

    public static byte[] sha256(InputStream data) throws IOException {
        return hash0(data, "SHA-256");
    }

    public static byte[] sha256(File data) throws IOException {
        return hash0(data, "SHA-256");
    }

    public static byte[] sha256(Path data, OpenOption... options) throws IOException {
        return hash0(data, "SHA-256", options);
    }

    public static String sha256Hex(byte[] data) {
        return hashAsHex0(data, "SHA-256");
    }

    public static String sha256Hex(String data) {
        return hashAsHex0(data, "SHA-256");
    }

    public static String sha256Hex(InputStream data) throws IOException {
        return hashAsHex0(data, "SHA-256");
    }

    public static String sha256Hex(File data) throws IOException {
        return hashAsHex0(data, "SHA-256");
    }

    public static String sha256Hex(Path data, OpenOption... options) throws IOException {
        return hashAsHex0(data, "SHA-256", options);
    }

    public static byte[] sha384(byte[] data) {
        return hash0(data, "SHA-384");
    }

    public static byte[] sha384(String data) {
        return hash0(data, "SHA-384");
    }

    public static byte[] sha384(InputStream data) throws IOException {
        return hash0(data, "SHA-384");
    }

    public static byte[] sha384(File data) throws IOException {
        return hash0(data, "SHA-384");
    }

    public static byte[] sha384(Path data, OpenOption... options) throws IOException {
        return hash0(data, "SHA-384", options);
    }

    public static String sha384Hex(byte[] data) {
        return hashAsHex0(data, "SHA-384");
    }

    public static String sha384Hex(String data) {
        return hashAsHex0(data, "SHA-384");
    }

    public static String sha384Hex(InputStream data) throws IOException {
        return hashAsHex0(data, "SHA-384");
    }

    public static String sha384Hex(File data) throws IOException {
        return hashAsHex0(data, "SHA-384");
    }

    public static String sha384Hex(Path data, OpenOption... options) throws IOException {
        return hashAsHex0(data, "SHA-384", options);
    }

    public static byte[] sha512(byte[] data) {
        return hash0(data, "SHA-512");
    }

    public static byte[] sha512(String data) {
        return hash0(data, "SHA-512");
    }

    public static byte[] sha512(InputStream data) throws IOException {
        return hash0(data, "SHA-512");
    }

    public static byte[] sha512(File data) throws IOException {
        return hash0(data, "SHA-512");
    }

    public static byte[] sha512(Path data, OpenOption... options) throws IOException {
        return hash0(data, "SHA-512", options);
    }

    public static String sha512Hex(byte[] data) {
        return hashAsHex0(data, "SHA-512");
    }

    public static String sha512Hex(String data) {
        return hashAsHex0(data, "SHA-512");
    }

    public static String sha512Hex(InputStream data) throws IOException {
        return hashAsHex0(data, "SHA-512");
    }

    public static String sha512Hex(File data) throws IOException {
        return hashAsHex0(data, "SHA-512");
    }

    public static String sha512Hex(Path data, OpenOption... options) throws IOException {
        return hashAsHex0(data, "SHA-512", options);
    }

    public static byte[] md5(byte[] data) {
        return hash0(data, "MD5");
    }

    public static byte[] md5(String data) {
        return hash0(data, "MD5");
    }

    public static byte[] md5(InputStream data) throws IOException {
        return hash0(data, "MD5");
    }

    public static byte[] md5(File data) throws IOException {
        return hash0(data, "MD5");
    }

    public static byte[] md5(Path data, OpenOption... options) throws IOException {
        return hash0(data, "MD5", options);
    }

    public static String md5Hex(byte[] data) {
        return hashAsHex0(data, "MD5");
    }

    public static String md5Hex(String data) {
        return hashAsHex0(data, "MD5");
    }

    public static String md5Hex(InputStream data) throws IOException {
        return hashAsHex0(data, "MD5");
    }

    public static String md5Hex(File data) throws IOException {
        return hashAsHex0(data, "MD5");
    }

    public static String md5Hex(Path data, OpenOption... options) throws IOException {
        return hashAsHex0(data, "MD5", options);
    }

    public static long crc32(byte[] data) {
        return hash(data, CRC32::new);
    }

    public static long crc32(String data) {
        return hash(data, CRC32::new);
    }

    public static long crc32(InputStream data) throws IOException {
        return hash(data, CRC32::new);
    }

    public static long crc32(File data) throws IOException {
        return hash(data, CRC32::new);
    }

    public static long crc32(Path data, OpenOption... options) throws IOException {
        return hash(data, CRC32::new, options);
    }

    public static String crc32Hex(byte[] data) {
        return hashAsHex(data, CRC32::new);
    }

    public static String crc32Hex(String data) {
        return hashAsHex(data, CRC32::new);
    }

    public static String crc32Hex(InputStream data) throws IOException {
        return hashAsHex(data, CRC32::new);
    }

    public static String crc32Hex(File data) throws IOException {
        return hashAsHex(data, CRC32::new);
    }

    public static String crc32Hex(Path data, OpenOption... options) throws IOException {
        return hashAsHex(data, CRC32::new, options);
    }

    public static long crc32c(byte[] data) {
        return hash(data, CRC32C::new);
    }

    public static long crc32c(String data) {
        return hash(data, CRC32C::new);
    }

    public static long crc32c(InputStream data) throws IOException {
        return hash(data, CRC32C::new);
    }

    public static long crc32c(File data) throws IOException {
        return hash(data, CRC32C::new);
    }

    public static long crc32c(Path data, OpenOption... options) throws IOException {
        return hash(data, CRC32C::new, options);
    }

    public static String crc32cHex(byte[] data) {
        return hashAsHex(data, CRC32C::new);
    }

    public static String crc32cHex(String data) {
        return hashAsHex(data, CRC32C::new);
    }

    public static String crc32cHex(InputStream data) throws IOException {
        return hashAsHex(data, CRC32C::new);
    }

    public static String crc32cHex(File data) throws IOException {
        return hashAsHex(data, CRC32C::new);
    }

    public static String crc32cHex(Path data, OpenOption... options) throws IOException {
        return hashAsHex(data, CRC32C::new, options);
    }

    /**
     * 此方法假定 指定算法一定存在 所以不向外显式抛出 {@link NoSuchAlgorithmException} 异常 . 下同
     *
     * @param data      data
     * @param algorithm algorithm
     * @return hash
     */
    private static byte[] hash0(byte[] data, String algorithm) {
        try {
            return hash(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] hash0(String data, String algorithm) {
        try {
            return hash(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] hash0(InputStream data, String algorithm) throws IOException {
        try {
            return hash(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] hash0(File data, String algorithm) throws IOException {
        try {
            return hash(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] hash0(Path data, String algorithm, OpenOption... options) throws IOException {
        try {
            return hash(data, algorithm, options);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String hashAsHex0(byte[] data, String algorithm) {
        try {
            return hashAsHex(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String hashAsHex0(String data, String algorithm) {
        try {
            return hashAsHex(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String hashAsHex0(InputStream data, String algorithm) throws IOException {
        try {
            return hashAsHex(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String hashAsHex0(File data, String algorithm) throws IOException {
        try {
            return hashAsHex(data, algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String hashAsHex0(Path data, String algorithm, OpenOption... options) throws IOException {
        try {
            return hashAsHex(data, algorithm, options);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static MessageDigest updateDigest(MessageDigest messageDigest, byte[] data) {
        messageDigest.update(data);
        return messageDigest;
    }

    private static MessageDigest updateDigest(MessageDigest messageDigest, String data) {
        messageDigest.update(data.getBytes(UTF_8));
        return messageDigest;
    }

    private static MessageDigest updateDigest(MessageDigest messageDigest, InputStream data) throws IOException {
        var buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read = data.read(buffer);
        while (read != -1) {
            messageDigest.update(buffer, 0, read);
            read = data.read(buffer);
        }
        return messageDigest;
    }

    private static MessageDigest updateDigest(MessageDigest digest, File data) throws IOException {
        try (var inputStream = new FileInputStream(data)) {
            return updateDigest(digest, inputStream);
        }
    }

    private static MessageDigest updateDigest(MessageDigest digest, Path data, OpenOption... options) throws IOException {
        try (var inputStream = Files.newInputStream(data, options)) {
            return updateDigest(digest, inputStream);
        }
    }

    private static Checksum updateChecksum(Checksum checksum, byte[] data) {
        checksum.update(data);
        return checksum;
    }

    private static Checksum updateChecksum(Checksum checksum, String data) {
        checksum.update(data.getBytes(UTF_8));
        return checksum;
    }

    private static Checksum updateChecksum(Checksum checksum, InputStream inputStream) throws IOException {
        var buffer = new byte[DEFAULT_BUFFER_SIZE];
        int read = inputStream.read(buffer);
        while (read != -1) {
            checksum.update(buffer, 0, read);
            read = inputStream.read(buffer);
        }
        return checksum;
    }

    private static Checksum updateChecksum(Checksum checksum, File data) throws IOException {
        try (var inputStream = new FileInputStream(data)) {
            return updateChecksum(checksum, inputStream);
        }
    }

    private static Checksum updateChecksum(Checksum checksum, Path data, OpenOption... options) throws IOException {
        try (var inputStream = Files.newInputStream(data, options)) {
            return updateChecksum(checksum, inputStream);
        }
    }

}
