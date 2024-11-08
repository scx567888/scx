package cool.scx.io.input_source;

import cool.scx.io.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Collections;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

/**
 * 用来压缩
 */
public class GzipInputSource extends LazyInputStreamInputSource {

    private final InputSource inputSource;

    public GzipInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    @Override
    protected InputStream toInputStream0() throws IOException {
        var crc = new CRC32();
        var def = new Deflater(Deflater.DEFAULT_COMPRESSION, true);

        var headerInputStream = new GzipHeaderInputStream();
        var dataInputStream = new GzipDataInputStream(inputSource.toInputStream(), crc, def);
        var trailerInputStream = new GzipTrailerInputStream(crc, def);

        //整合三个流
        var in = List.of(headerInputStream, dataInputStream, trailerInputStream);
        return new SequenceInputStream(Collections.enumeration(in));

    }

}
