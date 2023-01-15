package cool.scx.util.zip;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

public class GunzipBuilder extends ZipDataSource {

    public GunzipBuilder(Path path) {
        super(path);
    }

    public GunzipBuilder(byte[] bytes) {
        super(bytes);
    }

    public GunzipBuilder(Supplier<byte[]> bytesSupplier) {
        super(bytesSupplier);
    }

    public GunzipBuilder(InputStream inputStream) {
        super(inputStream);
    }

    public byte[] toBytes() throws Exception {
        try (var zos = new GZIPInputStream(toInputStream())) {
            return zos.readAllBytes();
        }
    }

    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPInputStream(toInputStream())) {
            zos.transferTo(Files.newOutputStream(outputPath));
        }
    }

}
