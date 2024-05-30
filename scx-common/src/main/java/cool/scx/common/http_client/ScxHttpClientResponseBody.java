package cool.scx.common.http_client;

import cool.scx.common.io_stream_source.RawInputStreamSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ScxHttpClientResponseBody extends RawInputStreamSource {

    private String string;
    private byte[] bytes;

    public ScxHttpClientResponseBody(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public String toString() {
        try {
            return this.toString(UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(Charset charset) throws IOException {
        if (this.string == null) {
            this.string = super.toString(UTF_8);
        }
        return this.string;
    }

    @Override
    public byte[] toBytes() throws IOException {
        if (this.bytes == null) {
            this.bytes = super.toBytes();
        }
        return this.bytes;
    }

}
