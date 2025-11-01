package cool.scx.http.media.empty;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.io.ByteOutput;

import java.io.IOException;
import java.io.OutputStream;

/// EmptyWriter
///
/// @author scx567888
/// @version 0.0.1
public class EmptyWriter implements MediaWriter {

    public static final EmptyWriter EMPTY_WRITER = new EmptyWriter();

    private EmptyWriter() {

    }

    @Override
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        return 0;
    }

    @Override
    public void write(ByteOutput byteOutput) throws IOException {
        try (byteOutput) {

        }
    }

}
