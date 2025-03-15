package cool.scx.http.media.input_stream;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

import java.io.InputStream;
import java.io.OutputStream;

/// InputStreamWriter
///
/// @author scx567888
/// @version 0.0.1
public class InputStreamWriter implements MediaWriter {

    private final InputStream inputStream;

    public InputStreamWriter(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        //什么都不做
    }

    @Override
    public void write(OutputStream outputStream) {
        try (inputStream; outputStream) {
            inputStream.transferTo(outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
