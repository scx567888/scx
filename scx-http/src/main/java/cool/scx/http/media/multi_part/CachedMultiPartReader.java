package cool.scx.http.media.multi_part;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpServerRequestHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;
import java.nio.file.Path;

public class CachedMultiPartReader implements MediaReader<CachedMultiPart> {
    
    public static final CachedMultiPartReader CACHED_MULTI_PART_READER =new CachedMultiPartReader();

    private final Path cachePath;

    public CachedMultiPartReader(Path cachePath) {
        this.cachePath = cachePath;
    }

    public CachedMultiPartReader() {
        this.cachePath = Path.of(System.getProperty("java.io.tmpdir"));
    }

    @Override
    public CachedMultiPart read(InputStream inputStream, ScxHttpServerRequestHeaders headers) {
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
