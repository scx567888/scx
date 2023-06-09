package cool.scx.util.zip;

import cool.scx.util.zip.zip_data_source.BytesSupplierZipDataSource;
import cool.scx.util.zip.zip_data_source.BytesZipDataSource;
import cool.scx.util.zip.zip_data_source.InputStreamZipDataSource;
import cool.scx.util.zip.zip_data_source.PathZipDataSource;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

/**
 * <p>GunzipBuilder class.</p>
 *
 * @author scx567888
 * @version 2.0.4
 */
public class GunzipBuilder {

    private final ZipDataSource zipDataSource;

    public GunzipBuilder(ZipDataSource zipDataSource) {
        this.zipDataSource = zipDataSource;
    }

    public GunzipBuilder(Path path) {
        this(new PathZipDataSource(path));
    }

    public GunzipBuilder(byte[] bytes) {
        this(new BytesZipDataSource(bytes));
    }

    public GunzipBuilder(Supplier<byte[]> bytesSupplier) {
        this(new BytesSupplierZipDataSource(bytesSupplier));
    }

    public GunzipBuilder(InputStream inputStream) {
        this(new InputStreamZipDataSource(inputStream));
    }

    public byte[] toBytes() throws Exception {
        try (var zos = new GZIPInputStream(this.zipDataSource.toInputStream())) {
            return zos.readAllBytes();
        }
    }

    /**
     * <p>toFile.</p>
     *
     * @param outputPath a {@link java.nio.file.Path} object
     * @throws java.lang.Exception if any.
     */
    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPInputStream(this.zipDataSource.toInputStream())) {
            zos.transferTo(Files.newOutputStream(outputPath));
        }
    }

}
