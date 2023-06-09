package cool.scx.util.zip.zip_data_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * 可以将其想象成一个 BytesSupplier ,主要用来规范多种类型的数据来源 如文件 字节数组等
 *
 * @author scx567888
 * @version 2.0.4
 */
public interface ZipDataSource {

    static ZipDataSource of(Path path) {
        return new PathZipDataSource(path);
    }

    static ZipDataSource of(byte[] bytes) {
        return new BytesZipDataSource(bytes);
    }

    static ZipDataSource of(Supplier<byte[]> bytesSupplier) {
        return new BytesSupplierZipDataSource(bytesSupplier);
    }

    static ZipDataSource of(InputStream inputStream) {
        return new InputStreamZipDataSource(inputStream);
    }

    void writeToOutputStream(OutputStream out) throws IOException;

    InputStream toInputStream() throws IOException;

}
