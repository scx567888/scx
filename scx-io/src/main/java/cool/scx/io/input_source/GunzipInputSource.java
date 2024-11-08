package cool.scx.io.input_source;

import cool.scx.io.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

/**
 * 用来解压缩
 */
public class GunzipInputSource implements InputSource {

    private final GZIPInputStream gzipInputStream;

    public GunzipInputSource(InputSource inputSource) throws IOException {
        this.gzipInputStream = new GZIPInputStream(inputSource.toInputStream());
    }

    @Override
    public byte[] read(int length) throws IOException {
        return gzipInputStream.readNBytes(length);
    }

    @Override
    public byte[] readAll() throws IOException {
        return gzipInputStream.readAllBytes();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return gzipInputStream.transferTo(out);
    }

    @Override
    public void transferTo(Path outputPath, OpenOption... options) throws IOException {
        try (var out = Files.newOutputStream(outputPath)) {
            gzipInputStream.transferTo(out);
        }
    }

    @Override
    public InputStream toInputStream() {
        return gzipInputStream;
    }

    @Override
    public void close() throws IOException {
        gzipInputStream.close();
    }

}
