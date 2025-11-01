package cool.scx.http.media.byte_array;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.io.ByteOutput;

import java.io.IOException;
import java.io.OutputStream;

/// ByteArrayWriter
///
/// @author scx567888
/// @version 0.0.1
public class ByteArrayWriter implements MediaWriter {

    private final byte[] bytes;

    public ByteArrayWriter(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        return bytes.length;
    }

    @Override
    public void write(ByteOutput byteOutput) throws IOException {
        try (byteOutput) {
            byteOutput.write(bytes);
        }
    }

}
