package cool.scx.http.media.multi_part;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;
import java.nio.file.Path;

import static cool.scx.http.media.multi_part.MultiPartStreamReader.checkedBoundary;

/// MultiPartStreamCachedReader
///
/// @author scx567888
/// @version 0.0.1
public class MultiPartStreamCachedReader implements MediaReader<MultiPart> {

    public static final MultiPartStreamCachedReader MULTI_PART_READER_CACHED = new MultiPartStreamCachedReader();

    private final Path cachePath;

    public MultiPartStreamCachedReader(Path cachePath) {
        this.cachePath = cachePath;
    }

    public MultiPartStreamCachedReader() {
        // 默认放在 操作系统临时目录下 .SCX-CACHE
        this.cachePath = Path.of(System.getProperty("java.io.tmpdir")).resolve(".SCX-CACHE");
    }

    @Override
    public MultiPart read(InputStream inputStream, ScxHttpHeaders headers) {
        var boundary = checkedBoundary(headers);
        return new MultiPartStreamCached(inputStream, boundary, cachePath);
    }

}
