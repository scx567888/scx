package cool.scx.http_client;

import cool.scx.util.io_stream_source.RawInputStreamSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ScxHttpClientResponseBody extends RawInputStreamSource {

    public ScxHttpClientResponseBody(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public String toString() {
        try {
            return toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
