package cool.scx.io.gzip;

import cool.scx.io.input_source.LazyInputStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

public class GzipTrailerInputStream extends LazyInputStream {

    /*
     * Trailer size in bytes.
     *
     */
    private static final int TRAILER_SIZE = 8;

    private final CRC32 crc;
    private final Deflater def;

    public GzipTrailerInputStream(CRC32 crc, Deflater def) {
        this.crc = crc;
        this.def = def;
    }

    @Override
    public InputStream toInputStream0() {
        byte[] trailer = new byte[TRAILER_SIZE];
        writeTrailer(trailer, 0);
        return new ByteArrayInputStream(trailer);
    }

    /*
     * Writes GZIP member trailer to a byte array, starting at a given
     * offset.
     */
    private void writeTrailer(byte[] buf, int offset) {
        writeInt((int) crc.getValue(), buf, offset); // CRC-32 of uncompr. data
        // RFC 1952: Size of the original (uncompressed) input data modulo 2^32
        int iSize = (int) def.getBytesRead();
        writeInt(iSize, buf, offset + 4);
    }

    /*
     * Writes integer in Intel byte order to a byte array, starting at a
     * given offset.
     */
    private void writeInt(int i, byte[] buf, int offset) {
        writeShort(i & 0xffff, buf, offset);
        writeShort((i >> 16) & 0xffff, buf, offset + 2);
    }

    /*
     * Writes short integer in Intel byte order to a byte array, starting
     * at a given offset
     */
    private void writeShort(int s, byte[] buf, int offset) {
        buf[offset] = (byte) (s & 0xff);
        buf[offset + 1] = (byte) ((s >> 8) & 0xff);
    }

}
