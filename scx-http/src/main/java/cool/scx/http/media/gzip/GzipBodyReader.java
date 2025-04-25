package cool.scx.http.media.gzip;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.IOException;
import java.io.InputStream;

public class GzipBodyReader implements MediaReader<GzipBody> {

    public static final GzipBodyReader GZIP_BODY_READER = new GzipBodyReader();

    private GzipBodyReader() {

    }

    @Override
    public GzipBody read(InputStream inputStream, ScxHttpHeaders requestHeaders) throws IOException {
        return new GzipBody(inputStream, requestHeaders);
    }

}
