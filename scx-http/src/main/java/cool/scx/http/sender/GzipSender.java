package cool.scx.http.sender;

import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.gzip.GzipWriter;

public class GzipSender<T> implements ScxHttpSender<T> {

    private final ScxHttpSender<T> sender;

    public GzipSender(ScxHttpSender<T> sender) {
        this.sender = sender;
    }

    public T send(MediaWriter writer) {
        return this.sender.send(new GzipWriter(writer));
    }

    @Override
    public ScxHttpSender<T> sendGzip() {
        return this;
    }
    
}
