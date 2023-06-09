package cool.scx.util.zip;

import cool.scx.util.zip.zip_data_source.ZipDataSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPOutputStream;

import static cool.scx.util.zip.zip_data_source.ZipDataSource.of;

/**
 * <p>GzipBuilder class.</p>
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class GzipBuilder {

    private final ZipDataSource zipDataSource;

    public GzipBuilder(ZipDataSource zipDataSource) {
        this.zipDataSource = zipDataSource;
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
            this.zipDataSource.writeToOutputStream(zos);
        }
        return bo.toByteArray();
    }

    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPOutputStream(Files.newOutputStream(outputPath))) {
            this.zipDataSource.writeToOutputStream(zos);
        }
    }

}
