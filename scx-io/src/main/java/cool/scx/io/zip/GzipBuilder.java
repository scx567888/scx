package cool.scx.io.zip;

import cool.scx.io.InputSource;
import cool.scx.io.OutputSource;

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
public final class GzipBuilder implements OutputSource {

    private final InputSource source;

    public GzipBuilder(InputSource source) {
        this.source = source;
    }

    public GzipBuilder(Path path) {
        this(InputSource.of(path));
    }

    public GzipBuilder(byte[] bytes) {
        this(InputSource.of(bytes));
    }

    public GzipBuilder(Supplier<byte[]> bytesSupplier) {
        this(InputSource.of(bytesSupplier));
    }

    public GzipBuilder(InputStream inputStream) {
        this(InputSource.of(inputStream));
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        try (var zos = new GZIPOutputStream(out)) {
            this.source.writeToOutputStream(zos);
        }
    }

}
