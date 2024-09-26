package cool.scx.http;

import java.io.InputStream;

import static cool.scx.http.media.MediaSupportSelector.findMediaSupport;

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
    public <T> T as(Class<T> t) {
        var mediaSupport = findMediaSupport(t);
        return mediaSupport.read(inputStream, headers);
    }

}
