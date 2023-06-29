package cool.scx.util.zip;

import cool.scx.util.input_stream_source.InputStreamSource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import static cool.scx.util.input_stream_source.InputStreamSource.of;

/**
 * <p>GunzipBuilder class.</p>
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class GunzipBuilder {

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

    public byte[] toBytes() throws Exception {
        try (var zos = new GZIPInputStream(this.source.toInputStream())) {
            return zos.readAllBytes();
        }
    }

    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPInputStream(this.source.toInputStream())) {
            zos.transferTo(Files.newOutputStream(outputPath));
        }
    }

}
