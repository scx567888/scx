package cool.scx.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.zip.CRC32;

import static cool.scx.util.HashUtils.CACHE_LENGTH;

public final class CRCUtils {

    /**
     * <p>crc32.</p>
     *
     * @param data a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String crc32(final String data) {
        return crc32(data != null ? data.getBytes(StandardCharsets.UTF_8) : null);
    }

    /**
     * <p>crc32.</p>
     *
     * @param data an array of {@link byte} objects
     * @return a {@link java.lang.String} object
     */
    public static String crc32(final byte[] data) {
        Objects.requireNonNull(data, "Data must not be empty !!!");
        var crc32 = new CRC32();
        crc32.update(data);
        return Long.toHexString(crc32.getValue());
    }

    /**
     * <p>crc32.</p>
     *
     * @param data a {@link java.io.File} object
     * @return a {@link java.lang.String} object
     * @throws java.io.IOException if any.
     */
    public static String crc32(final File data) throws IOException {
        Objects.requireNonNull(data, "Data must not be empty !!!");
        var crc32 = new CRC32();
        var buffer = new byte[CACHE_LENGTH];
        int read;
        try (var file = new RandomAccessFile(data, "r")) {
            while ((read = file.read(buffer, 0, CACHE_LENGTH)) != -1) {
                crc32.update(buffer, 0, read);
            }
        }
        return Long.toHexString(crc32.getValue());
    }

}
