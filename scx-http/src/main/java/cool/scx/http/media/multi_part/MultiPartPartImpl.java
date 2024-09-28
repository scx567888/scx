package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;

import java.io.InputStream;
import java.util.function.Supplier;

public class MultiPartPartImpl implements MultiPartPartWritable {

    private ScxHttpHeadersWritable headers;
    private Supplier<InputStream> body;

    public MultiPartPartImpl() {
        this.headers = ScxHttpHeaders.of();
    }

    @Override
    public MultiPartPartWritable headers(ScxHttpHeadersWritable headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public MultiPartPartWritable body(Supplier<InputStream> os) {
        body = os;
        return this;
    }

    @Override
    public ScxHttpHeadersWritable headers() {
        return headers;
    }

    @Override
    public Supplier<InputStream> body() {
        return body;
    }

}
