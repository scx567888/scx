package cool.scx.io.input_source;

import cool.scx.io.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;

/**
 * 用来压缩
 */
public class GzipInputSource extends DeflaterInputStream implements InputSource {

    /**
     * CRC-32 of uncompressed data.
     */
    protected CRC32 crc = new CRC32();

    /*
     * GZIP header magic number.
     */
    private static final int GZIP_MAGIC = 0x8b1f;

    /*
     * Trailer size in bytes.
     *
     */
    private static final int TRAILER_SIZE = 8;

    // Represents the default "unknown" value for OS header, per RFC-1952
    private static final byte OS_UNKNOWN = (byte) 255;

    private static final byte[] HEADER = new byte[]{
            (byte) GZIP_MAGIC,         // Magic number (short)
            (byte) (GZIP_MAGIC >> 8),  // Magic number (short)
            Deflater.DEFLATED,         // Compression method (CM)
            0,                         // Flags (FLG)
            0,                         // Modification time MTIME (int)
            0,                         // Modification time MTIME (int)
            0,                         // Modification time MTIME (int)
            0,                         // Modification time MTIME (int)
            0,                         // Extra flags (XFLG)
            OS_UNKNOWN                 // Operating system (OS)
    };

    private final ByteArrayInputStream headerInputStream = new ByteArrayInputStream(HEADER);

    public GzipInputSource(InputSource inputSource) throws IOException {
        super(inputSource.toInputStream());
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = headerInputStream.read(b, off, len);
        if (read > 0) {
            return read;
        }
        crc.update(b, off, len);
        var i= super.read(b, off, len);
        if (i > 0) {
            return i;
        }
        // if we can't fit the trailer at the end of the last
        // deflater buffer, we write it separately
        byte[] trailer = new byte[TRAILER_SIZE];
        writeTrailer(trailer, 0);
        return -1;
    }

    @Override
    public byte[] read(int length) throws IOException {
        return readNBytes(length);
    }

    @Override
    public byte[] readAll() throws IOException {
        return readAllBytes();
    }

    @Override
    public void transferTo(Path outputPath, OpenOption... options) throws IOException {
        try(var out= Files.newOutputStream(outputPath, options)){
            this.transferTo(out);
        }
    }

    @Override
    public InputStream toInputStream() throws IOException {
        return this;
    }


    /*
     * Writes GZIP member trailer to a byte array, starting at a given
     * offset.
     */
    private void writeTrailer(byte[] buf, int offset) throws IOException {
        writeInt((int)crc.getValue(), buf, offset); // CRC-32 of uncompr. data
        // RFC 1952: Size of the original (uncompressed) input data modulo 2^32
        int iSize = (int) def.getBytesRead();
        writeInt(iSize, buf, offset + 4);
    }

    /*
     * Writes integer in Intel byte order to a byte array, starting at a
     * given offset.
     */
    private void writeInt(int i, byte[] buf, int offset) throws IOException {
        writeShort(i & 0xffff, buf, offset);
        writeShort((i >> 16) & 0xffff, buf, offset + 2);
    }

    /*
     * Writes short integer in Intel byte order to a byte array, starting
     * at a given offset
     */
    private void writeShort(int s, byte[] buf, int offset) throws IOException {
        buf[offset] = (byte)(s & 0xff);
        buf[offset + 1] = (byte)((s >> 8) & 0xff);
    }

}
