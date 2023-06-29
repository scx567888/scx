package cool.scx.util.io_stream_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 可以将其想象成一个 BytesSupplier ,主要用来规范多种类型的数据来源 如文件 字节数组等
 *
 * @author scx567888
 * @version 2.0.4
 */
public interface InputStreamSource extends OutputStreamSource {

    static InputStreamSource of() {
        return new NullSource();
    }

    static InputStreamSource of(Path path) {
        return new PathSource(path);
    }

    static InputStreamSource of(byte[] bytes) {
        return new BytesSource(bytes);
    }

    static InputStreamSource of(Supplier<byte[]> bytesSupplier) {
        return new BytesSupplierSource(bytesSupplier);
    }

    static InputStreamSource of(InputStream inputStream) {
        return new RawInputStreamSource(inputStream);
    }

    static InputStreamSource of(ZipEntry zipEntry, ZipFile zipFile) {
        return new ZipEntrySource(zipEntry, zipFile);
    }

    /**
     * 转换为  InputStream
     *
     * @return InputStream
     * @throws IOException e
     */
    InputStream toInputStream() throws IOException;

    @Override
    default byte[] toBytes() throws IOException {
        try (var inputStream = toInputStream()) {
            return inputStream.readAllBytes();
        }
    }

    @Override
    default void writeToOutputStream(OutputStream out) throws IOException {
        try (var inputStream = toInputStream()) {
            inputStream.transferTo(out);
        }
    }

}
