package cool.scx.util.zip;

import cool.scx.functional.ScxHandlerR;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static cool.scx.util.zip.ZipDataSource.Type.*;

/**
 * 可以将其想象成一个 BytesSupplier ,主要用来规范多种类型的数据来源 如文件 字节数组等
 */
abstract class ZipDataSource {

    protected final ZipDataSource.Type type;
    protected Path path;
    protected byte[] bytes;
    protected ScxHandlerR<byte[]> bytesSupplier;
    protected InputStream inputStream;

    protected ZipDataSource(Path path) {
        this.path = path;
        this.type = PATH;
    }

    protected ZipDataSource(byte[] bytes) {
        this.bytes = bytes;
        this.type = BYTES;
    }

    protected ZipDataSource(ScxHandlerR<byte[]> bytesSupplier) {
        this.bytesSupplier = bytesSupplier;
        this.type = BYTES_SUPPLIER;
    }

    protected ZipDataSource(InputStream inputStream) {
        this.inputStream = inputStream;
        this.type = INPUT_STREAM;
    }

    protected ZipDataSource() {
        this.type = NULL;
    }

    public void writeToOutputStream(OutputStream out) throws IOException {
        switch (type) {
            case NULL -> {
            }
            case PATH -> Files.copy(path, out);
            case BYTES -> out.write(bytes);
            case BYTES_SUPPLIER -> out.write(bytesSupplier.handle());
            case INPUT_STREAM -> inputStream.transferTo(out);
        }
    }

    public InputStream toInputStream() throws IOException {
        return switch (type) {
            case NULL -> InputStream.nullInputStream();
            case PATH -> Files.newInputStream(path);
            case BYTES -> new ByteArrayInputStream(bytes);
            case BYTES_SUPPLIER -> new ByteArrayInputStream(bytesSupplier.handle());
            case INPUT_STREAM -> inputStream;
        };
    }

    public enum Type {
        NULL,
        PATH,
        BYTES,
        BYTES_SUPPLIER,
        INPUT_STREAM,
    }

}
