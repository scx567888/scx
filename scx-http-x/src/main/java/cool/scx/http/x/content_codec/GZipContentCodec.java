package cool.scx.http.x.content_codec;

import cool.scx.http.exception.UnsupportedMediaTypeException;
import cool.scx.http.headers.content_encoding.ScxContentEncoding;
import cool.scx.io.zip.GunzipBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static cool.scx.http.headers.content_encoding.ContentEncoding.GZIP;

public class GZipContentCodec implements HttpContentCodec {

    public static final GZipContentCodec GZIP_CONTENT_CODEC = new GZipContentCodec();

    private GZipContentCodec() {

    }

    @Override
    public boolean canHandle(ScxContentEncoding contentEncoding) {
        return contentEncoding == GZIP;
    }

    @Override
    public InputStream decode(InputStream inputStream) {
        try {
            return new GunzipBuilder(inputStream);
        } catch (IOException e) {
            throw new UnsupportedMediaTypeException(e);
        }
    }

    @Override
    public OutputStream encode(OutputStream outputStream) {
        //todo 待处理
        return null;
    }

}
