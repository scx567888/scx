package cool.scx.http.media.multi_part;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.io.ByteInput;

import java.io.InputStream;
import java.util.function.Supplier;

/// MultiPartPartImpl
///
/// @author scx567888
/// @version 0.0.1
public class MultiPartPartImpl implements MultiPartPartWritable {

    private ScxHttpHeadersWritable headers;
    private Supplier<ByteInput> body;

    public MultiPartPartImpl() {
        this.headers = ScxHttpHeaders.of();
    }

    @Override
    public MultiPartPartWritable headers(ScxHttpHeaders headers) {
        this.headers = ScxHttpHeaders.of(headers);
        return this;
    }

    @Override
    public MultiPartPartWritable body(Supplier<ByteInput> os) {
        body = os;
        return this;
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return headers;
    }

    @Override
    public Supplier<ByteInput> body() {
        return body;
    }

}
