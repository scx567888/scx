package cool.scx.http.media.empty;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

import java.io.OutputStream;

/**
 * EmptyWriter
 *
 * @author scx567888
 * @version 0.0.1
 */
public class EmptyWriter implements MediaWriter {

    @Override
    public void beforeWrite(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers) {

    }

    @Override
    public void write(OutputStream outputStream) {
        try (outputStream) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
