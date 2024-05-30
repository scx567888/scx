package cool.scx.common.zip;

import cool.scx.common.io_stream_source.InputStreamSource;
import cool.scx.common.io_stream_source.OutputStreamSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPOutputStream;

/**
 * GzipBuilder
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class GzipBuilder implements OutputStreamSource {

    private final InputStreamSource source;

    public GzipBuilder(InputStreamSource source) {
        this.source = source;
    }

    public GzipBuilder(Path path) {
        this(InputStreamSource.of(path));
    }

    public GzipBuilder(byte[] bytes) {
        this(InputStreamSource.of(bytes));
    }

    public GzipBuilder(Supplier<byte[]> bytesSupplier) {
        this(InputStreamSource.of(bytesSupplier));
    }

    public GzipBuilder(InputStream inputStream) {
        this(InputStreamSource.of(inputStream));
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        try (var zos = new GZIPOutputStream(out)) {
            this.source.writeToOutputStream(zos);
        }
    }

}
