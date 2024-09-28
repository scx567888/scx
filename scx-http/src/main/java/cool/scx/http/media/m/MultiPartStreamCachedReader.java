package cool.scx.http.media.m;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;
import cool.scx.http.media.multi_part.CachedMultiPart;

import java.io.InputStream;
import java.nio.file.Path;

public class MultiPartStreamCachedReader implements MediaReader<CachedMultiPart> {

    public static final MultiPartStreamCachedReader CACHED_MULTI_PART_READER = new MultiPartStreamCachedReader();

    private final Path cachePath;

    public MultiPartStreamCachedReader(Path cachePath) {
        this.cachePath = cachePath;
    }

    public MultiPartStreamCachedReader() {
        this.cachePath = Path.of(System.getProperty("java.io.tmpdir")).resolve(".SCX-CACHE");
    }

    @Override
    public CachedMultiPart read(InputStream inputStream, ScxHttpHeaders headers) {
        var contentType = headers.contentType();
        if (contentType == null) {
            throw new IllegalArgumentException("No Content-Type header found");
        }
        if (contentType.mediaType() != MediaType.MULTIPART_FORM_DATA) {
            throw new IllegalArgumentException("Content-Type is not multipart/form-data");
        }
        var boundary = contentType.boundary();
        if (boundary == null) {
            throw new IllegalArgumentException("No boundary found");
        }
        return new CachedMultiPart(inputStream, boundary, cachePath);
    }

}
