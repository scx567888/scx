package cool.scx.util.zip;

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
public class GzipBuilder extends ZipDataSource {

    /**
     * <p>Constructor for GzipBuilder.</p>
     *
     * @param path a {@link Path} object
     */
    public GzipBuilder(Path path) {
        super(path);
    }

    /**
     * <p>Constructor for GzipBuilder.</p>
     *
     * @param bytes an array of {@link byte} objects
     */
    public GzipBuilder(byte[] bytes) {
        super(bytes);
    }

    /**
     * <p>Constructor for GzipBuilder.</p>
     *
     * @param bytesSupplier a {@link Supplier} object
     */
    public GzipBuilder(Supplier<byte[]> bytesSupplier) {
        super(bytesSupplier);
    }

    /**
     * <p>Constructor for GzipBuilder.</p>
     *
     * @param inputStream a {@link InputStream} object
     */
    public GzipBuilder(InputStream inputStream) {
        super(inputStream);
    }

    /**
     * <p>toBytes.</p>
     *
     * @return an array of {@link byte} objects
     * @throws Exception if any.
     */
    public byte[] toBytes() throws Exception {
        var bo = new ByteArrayOutputStream();
        try (var zos = new GZIPOutputStream(bo)) {
            this.writeToOutputStream(zos);
        }
        return bo.toByteArray();
    }

    /**
     * <p>toFile.</p>
     *
     * @param outputPath a {@link Path} object
     * @throws Exception if any.
     */
    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPOutputStream(Files.newOutputStream(outputPath))) {
            this.writeToOutputStream(zos);
        }
    }

}
