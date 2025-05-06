package cool.scx.http.media.gzip;

import cool.scx.http.media.MediaWriter;
import cool.scx.http.sender.ScxHttpSender;

public class GzipSender<T> implements ScxHttpSender<T> {

    private final ScxHttpSender<T> httpSender;

    public GzipSender(ScxHttpSender<T> httpSender) {
        this.httpSender = httpSender;
    }

    public T send(MediaWriter writer) {
        return this.httpSender.send(new GzipWriter(writer));
    }

}
