package cool.scx.io.gzip;

import cool.scx.io.InputSource;
import cool.scx.io.input_source.LazyInputStreamInputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * 用来解压缩
 */
public class GunzipInputSource extends LazyInputStreamInputSource {

    private final InputSource inputSource;

    public GunzipInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    @Override
    public InputStream toInputStream0() throws IOException {
        return new GZIPInputStream(inputSource.toInputStream());
    }

}
