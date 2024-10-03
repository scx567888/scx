package cool.scx.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ScxHttpBodyImpl implements ScxHttpBody {

    private final InputStream inputStream;
    private final ScxHttpHeaders headers;

    // InputStream 的缓存
    private final BufferedInputStream bufferedInputStream;

    public ScxHttpBodyImpl(InputStream inputStream, ScxHttpHeaders headers) {
        this.inputStream = inputStream;
        this.bufferedInputStream = new BufferedInputStream(inputStream);
        this.headers = headers;
        this.bufferedInputStream.mark(0);
    }

    @Override
    public ScxHttpHeaders headers() {
        return headers;
    }

    @Override
    public InputStream inputStream() {
        try {
            bufferedInputStream.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bufferedInputStream;
    }

}
