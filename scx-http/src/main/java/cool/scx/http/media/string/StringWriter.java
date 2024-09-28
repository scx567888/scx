package cool.scx.http.media.string;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;


public class StringWriter implements MediaWriter {

    private final byte[] bytes;

    public StringWriter(String str) {
        this.bytes = str.getBytes(UTF_8);
    }

    public StringWriter(String str, Charset charset) {
        this.bytes = str.getBytes(charset);
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        responseHeaders.contentLength(bytes.length);
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
