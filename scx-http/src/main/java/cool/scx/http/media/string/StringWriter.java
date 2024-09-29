package cool.scx.http.media.string;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.media.MediaWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static cool.scx.http.MediaType.TEXT_PLAIN;
import static java.nio.charset.StandardCharsets.UTF_8;


public class StringWriter implements MediaWriter {

    private final Charset charset;
    private final byte[] bytes;

    public StringWriter(String str) {
        this.charset = UTF_8;
        this.bytes = str.getBytes(UTF_8);
    }

    public StringWriter(String str, Charset charset) {
        this.charset = charset;
        this.bytes = str.getBytes(charset);
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        if (responseHeaders.contentLength() == null) {
            responseHeaders.contentLength(bytes.length);
        }
        if (responseHeaders.contentType() == null) {
            responseHeaders.contentType(ContentType.of(TEXT_PLAIN).charset(charset));
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