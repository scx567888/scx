package cool.scx.io.io_stream_source;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public interface OutputSource {

    default void toFile(Path outputPath, OpenOption... options) throws IOException {
        Files.createDirectories(outputPath.getParent());
        try (var fo = Files.newOutputStream(outputPath, options)) {
            writeToOutputStream(fo);
        }
    }

    default byte[] toBytes() throws IOException {
        try (var bo = new ByteArrayOutputStream()) {
            writeToOutputStream(bo);
            return bo.toByteArray();
        }
    }

    /**
     * 写入到指定输出流
     *
     * @param out 输出流
     * @throws IOException e
     */
    void writeToOutputStream(OutputStream out) throws IOException;

}
