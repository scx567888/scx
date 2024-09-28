package cool.scx.http.media.multi_part;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;
import java.nio.file.Path;

public class MultiPartStreamCachedReader implements MediaReader<MultiPart> {

    public static final MultiPartStreamCachedReader CACHED_MULTI_PART_READER = new MultiPartStreamCachedReader();

    private final Path cachePath;

    public MultiPartStreamCachedReader(Path cachePath) {
        this.cachePath = cachePath;
    }

    public MultiPartStreamCachedReader() {
        this.cachePath = Path.of(System.getProperty("java.io.tmpdir")).resolve(".SCX-CACHE");
    }

    @Override
    public MultiPart read(InputStream inputStream, ScxHttpHeaders headers) {
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
        return new MultiPartStreamCached(inputStream, boundary, cachePath);
    }

}
