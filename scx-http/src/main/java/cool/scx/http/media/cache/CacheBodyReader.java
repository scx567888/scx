package cool.scx.http.media.cache;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.IOException;
import java.io.InputStream;

public class CacheBodyReader implements MediaReader<CacheBody> {

    public static final CacheBodyReader CACHE_BODY_READER = new CacheBodyReader();

    private CacheBodyReader() {

    }

    @Override
    public CacheBody read(InputStream inputStream, ScxHttpHeaders requestHeaders) throws IOException {
        return new CacheBody(inputStream, requestHeaders);
    }

}
