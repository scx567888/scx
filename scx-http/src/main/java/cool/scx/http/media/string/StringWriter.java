package cool.scx.http.media.string;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media_type.ScxMediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

import static cool.scx.http.media_type.MediaType.TEXT_PLAIN;
import static java.nio.charset.StandardCharsets.UTF_8;

/// StringWriter
///
/// @author scx567888
/// @version 0.0.1
public class StringWriter implements MediaWriter {

    private final Charset charset;
    private final String str;
    private byte[] bytes;

    public StringWriter(String str) {
        this(str, UTF_8);
    }

    public StringWriter(String str, Charset charset) {
        this.str = str;
        this.charset = charset;
        this.bytes = null;
    }

    @Override
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        // 只有在没设置 contentType 的时候我们才主动设置 
        if (responseHeaders.contentType() == null) {
            responseHeaders.contentType(ScxMediaType.of(TEXT_PLAIN).charset(charset));
        }
        bytes = str.getBytes(charset);
        return bytes.length;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        try (outputStream) {
            outputStream.write(bytes);
        }
    }

}
