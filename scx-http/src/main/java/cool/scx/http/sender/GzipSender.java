package cool.scx.http.sender;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import static cool.scx.http.headers.content_encoding.ContentEncoding.GZIP;

public class GzipSender<T> implements ScxHttpSender<T> {

    private final ScxHttpSender<T> httpSender;

    public GzipSender(ScxHttpSender<T> httpSender) {
        this.httpSender = httpSender;
    }

    public T send(MediaWriter writer) {
        return this.httpSender.send(new GzipWriter(writer));
    }

    private record GzipWriter(MediaWriter mediaWriter) implements MediaWriter {

        @Override
        public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
            responseHeaders.contentEncoding(GZIP);
            mediaWriter.beforeWrite(responseHeaders, requestHeaders);
            return -1;
        }

        @Override
        public void write(OutputStream outputStream) throws IOException {
            var gzipOutputStream = new GZIPOutputStream(outputStream);
            mediaWriter.write(gzipOutputStream);
        }

    }

}
