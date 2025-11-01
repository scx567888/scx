package cool.scx.http.media.gzip;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.io.ByteInput;
import cool.scx.io.ByteOutput;
import cool.scx.io.OutputStreamByteOutput;
import cool.scx.io.adapter.ByteOutputAdapter;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import static cool.scx.http.headers.content_encoding.ContentEncoding.GZIP;

// todo 这个 还可以支持 指定的长度 前提是先压缩然后发送字节
public record GzipWriter(MediaWriter mediaWriter) implements MediaWriter {

    @Override
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        responseHeaders.contentEncoding(GZIP);
        mediaWriter.beforeWrite(responseHeaders, requestHeaders);
        return -1;
    }

    @Override
    public void write(ByteOutput byteOutput) throws ScxIOException, AlreadyClosedException {
        GZIPOutputStream gzipOutputStream = null;
        try {
            gzipOutputStream = new GZIPOutputStream(ByteOutputAdapter.byteOutputToOutputStream(byteOutput));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var gzipByteOutput = new OutputStreamByteOutput(gzipOutputStream);
        mediaWriter.write(gzipByteOutput);
    }

}
