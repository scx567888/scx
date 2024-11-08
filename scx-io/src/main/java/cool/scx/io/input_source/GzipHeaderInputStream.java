package cool.scx.io.input_source;

import java.io.ByteArrayInputStream;
import java.util.zip.Deflater;

public class GzipHeaderInputStream extends ByteArrayInputStream {

    /*
     * GZIP header magic number.
     */
    private static final int GZIP_MAGIC = 0x8b1f;

    // Represents the default "unknown" value for OS header, per RFC-1952
    private static final byte OS_UNKNOWN = (byte) 255;

    // HEADER
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

    public GzipHeaderInputStream() {
        super(HEADER);
    }

}
