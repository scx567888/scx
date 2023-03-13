package cool.scx.util.zip;

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
public class GunzipBuilder extends ZipDataSource {

    /**
     * <p>Constructor for GunzipBuilder.</p>
     *
     * @param path a {@link Path} object
     */
    public GunzipBuilder(Path path) {
        super(path);
    }

    /**
     * <p>Constructor for GunzipBuilder.</p>
     *
     * @param bytes an array of {@link byte} objects
     */
    public GunzipBuilder(byte[] bytes) {
        super(bytes);
    }

    /**
     * <p>Constructor for GunzipBuilder.</p>
     *
     * @param bytesSupplier a {@link Supplier} object
     */
    public GunzipBuilder(Supplier<byte[]> bytesSupplier) {
        super(bytesSupplier);
    }

    /**
     * <p>Constructor for GunzipBuilder.</p>
     *
     * @param inputStream a {@link InputStream} object
     */
    public GunzipBuilder(InputStream inputStream) {
        super(inputStream);
    }

    /**
     * <p>toBytes.</p>
     *
     * @return an array of {@link byte} objects
     * @throws Exception if any.
     */
    public byte[] toBytes() throws Exception {
        try (var zos = new GZIPInputStream(toInputStream())) {
            return zos.readAllBytes();
        }
    }

    /**
     * <p>toFile.</p>
     *
     * @param outputPath a {@link Path} object
     * @throws Exception if any.
     */
    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPInputStream(toInputStream())) {
            zos.transferTo(Files.newOutputStream(outputPath));
        }
    }

}
