package cool.scx.util.zip;

import cool.scx.util.zip.zip_data_source.BytesSupplierZipDataSource;
import cool.scx.util.zip.zip_data_source.BytesZipDataSource;
import cool.scx.util.zip.zip_data_source.InputStreamZipDataSource;
import cool.scx.util.zip.zip_data_source.PathZipDataSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPOutputStream;

/**
 * <p>GzipBuilder class.</p>
 *
 * @author scx567888
 * @version 2.0.4
 */
public class GzipBuilder {

    private final ZipDataSource zipDataSource;

    public GzipBuilder(ZipDataSource zipDataSource) {
        this.zipDataSource = zipDataSource;
    }

    public GzipBuilder(Path path) {
        this(new PathZipDataSource(path));
    }

    public GzipBuilder(byte[] bytes) {
        this(new BytesZipDataSource(bytes));
    }

    public GzipBuilder(Supplier<byte[]> bytesSupplier) {
        this(new BytesSupplierZipDataSource(bytesSupplier));
    }

    public GzipBuilder(InputStream inputStream) {
        this(new InputStreamZipDataSource(inputStream));
    }

    /**
     * <p>toBytes.</p>
     *
     * @return an array of {@link byte} objects
     * @throws java.lang.Exception if any.
     */
    public byte[] toBytes() throws Exception {
        var bo = new ByteArrayOutputStream();
        try (var zos = new GZIPOutputStream(bo)) {
            this.zipDataSource.writeToOutputStream(zos);
        }
        return bo.toByteArray();
    }

    /**
     * <p>toFile.</p>
     *
     * @param outputPath a {@link java.nio.file.Path} object
     * @throws java.lang.Exception if any.
     */
    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPOutputStream(Files.newOutputStream(outputPath))) {
            this.zipDataSource.writeToOutputStream(zos);
        }
    }

}
