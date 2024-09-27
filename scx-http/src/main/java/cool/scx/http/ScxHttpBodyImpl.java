package cool.scx.http;

import cool.scx.http.media.MediaReader;

import java.io.InputStream;

public class ScxHttpBodyImpl implements ScxHttpBody {

    private final InputStream inputStream;
    private final ScxHttpServerRequestHeaders headers;

    public ScxHttpBodyImpl(InputStream inputStream, ScxHttpServerRequestHeaders headers) {
        this.inputStream = inputStream;
        this.headers = headers;
    }

    @Override
    public InputStream inputStream() {
        return inputStream;
    }

    @Override
    public <T> T as(MediaReader<T> t) {
        return t.read(inputStream, headers);
    }

}
