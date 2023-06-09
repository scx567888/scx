package cool.scx.util.zip;

import cool.scx.util.zip.zip_data_source.ZipDataSource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import static cool.scx.util.zip.zip_data_source.ZipDataSource.*;

/**
 * <p>GunzipBuilder class.</p>
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class GunzipBuilder {

    private final ZipDataSource zipDataSource;

    public GunzipBuilder(ZipDataSource zipDataSource) {
        this.zipDataSource = zipDataSource;
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
        try (var zos = new GZIPInputStream(this.zipDataSource.toInputStream())) {
            return zos.readAllBytes();
        }
    }

    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPInputStream(this.zipDataSource.toInputStream())) {
            zos.transferTo(Files.newOutputStream(outputPath));
        }
    }

}
