package cool.scx.http.media.byte_array;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

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
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        if (responseHeaders.contentLength() == null) {
            responseHeaders.contentLength(bytes.length);
        }
    }

    @Override
    public void write(OutputStream outputStream) {
        try (outputStream) {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
