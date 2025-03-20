package cool.scx.http.media.empty;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/// EmptyWriter
///
/// @author scx567888
/// @version 0.0.1
public class EmptyWriter implements MediaWriter {

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {

    }

    @Override
    public void write(OutputStream outputStream) {
        try (outputStream) {

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
