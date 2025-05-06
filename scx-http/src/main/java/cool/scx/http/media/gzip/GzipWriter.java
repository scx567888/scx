package cool.scx.http.media.gzip;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import static cool.scx.http.headers.content_encoding.ContentEncoding.GZIP;

public record GzipWriter(MediaWriter mediaWriter) implements MediaWriter {

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
