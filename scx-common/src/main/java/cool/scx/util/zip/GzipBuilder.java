package cool.scx.util.zip;

import cool.scx.functional.ScxHandlerR;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

public class GzipBuilder extends ZipDataSource {

    public GzipBuilder(Path path) {
        super(path);
    }

    public GzipBuilder(byte[] bytes) {
        super(bytes);
    }

    public GzipBuilder(ScxHandlerR<byte[]> bytesSupplier) {
        super(bytesSupplier);
    }

    public GzipBuilder(InputStream inputStream) {
        super(inputStream);
    }

    public byte[] toBytes() throws Exception {
        var bo = new ByteArrayOutputStream();
        try (var zos = new GZIPOutputStream(bo)) {
            this.writeToOutputStream(zos);
        }
        return bo.toByteArray();
    }

    public void toFile(Path outputPath) throws Exception {
        Files.createDirectories(outputPath.getParent());
        try (var zos = new GZIPOutputStream(Files.newOutputStream(outputPath))) {
            this.writeToOutputStream(zos);
        }
    }

}
