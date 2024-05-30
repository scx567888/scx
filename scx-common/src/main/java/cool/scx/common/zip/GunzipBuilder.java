package cool.scx.common.zip;

import cool.scx.common.io_stream_source.InputStreamSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import static cool.scx.common.io_stream_source.InputStreamSource.of;

/**
 * GunzipBuilder
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class GunzipBuilder implements InputStreamSource {

    private final InputStreamSource source;

    public GunzipBuilder(InputStreamSource source) {
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
