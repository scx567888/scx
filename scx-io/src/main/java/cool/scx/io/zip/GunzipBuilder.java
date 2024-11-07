package cool.scx.io.zip;

import cool.scx.io.io_stream_source.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import static cool.scx.io.io_stream_source.InputSource.of;

/**
 * GunzipBuilder
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class GunzipBuilder implements InputSource {

    private final InputSource source;

    public GunzipBuilder(InputSource source) {
        this.source = source;
    }

    public GunzipBuilder(Path path) {
        this(of(path));
    }

    public GunzipBuilder(byte[] bytes) {
        this(of(bytes));
    }

    public GunzipBuilder(Supplier<byte[]> bytesSupplier) {
        this(of(bytesSupplier));
    }

    public GunzipBuilder(InputStream inputStream) {
        this(of(inputStream));
    }

    @Override
    public InputStream toInputStream() throws IOException {
        return new GZIPInputStream(this.source.toInputStream());
    }

}
