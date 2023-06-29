package cool.scx.util.zip;

import cool.scx.util.input_stream_source.InputStreamSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPOutputStream;

import static cool.scx.util.input_stream_source.InputStreamSource.of;

/**
 * <p>GzipBuilder class.</p>
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class GzipBuilder {

    private final InputStreamSource source;

    public GzipBuilder(InputStreamSource source) {
        this.source = source;
    }

    public GzipBuilder(Path path) {
        this(of(path));
    }

    public GzipBuilder(byte[] bytes) {
        this(of(bytes));
    }

    public GzipBuilder(Supplier<byte[]> bytesSupplier) {
        this(of(bytesSupplier));
    }

    public GzipBuilder(InputStream inputStream) {
        this(of(inputStream));
    }

    public byte[] toBytes() throws Exception {
        var bo = new ByteArrayOutputStream();
        try (var zos = new GZIPOutputStream(bo)) {
            this.source.writeToOutputStream(zos);
        }
        return bo.toByteArray();
    }

    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPOutputStream(Files.newOutputStream(outputPath))) {
            this.source.writeToOutputStream(zos);
        }
    }

}
