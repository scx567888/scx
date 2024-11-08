package cool.scx.io.input_source;

import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterInputStream;

public class GzipDataInputStream extends DeflaterInputStream {

    public GzipDataInputStream(InputStream in, CRC32 crc, Deflater def) {
        super(new CheckedInputStream(in, crc), def);
    }

}
