package cool.scx.util.zip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import static cool.scx.util.zip.ZipDataSource.Type.*;

/**
 * 可以将其想象成一个 BytesSupplier ,主要用来规范多种类型的数据来源 如文件 字节数组等
 *
 * @author scx567888
 * @version 2.0.4
 */
abstract class ZipDataSource {

    protected final ZipDataSource.Type type;
    protected Path path;
    protected byte[] bytes;
    protected Supplier<byte[]> bytesSupplier;
    protected InputStream inputStream;

    /**
     * <p>Constructor for ZipDataSource.</p>
     *
     * @param path a {@link java.nio.file.Path} object
     */
    protected ZipDataSource(Path path) {
        this.path = path;
        this.type = PATH;
    }

    /**
     * <p>Constructor for ZipDataSource.</p>
     *
     * @param bytes an array of {@link byte} objects
     */
    protected ZipDataSource(byte[] bytes) {
        this.bytes = bytes;
        this.type = BYTES;
    }

    /**
     * <p>Constructor for ZipDataSource.</p>
     *
     * @param bytesSupplier a {@link java.util.function.Supplier} object
     */
    protected ZipDataSource(Supplier<byte[]> bytesSupplier) {
        this.bytesSupplier = bytesSupplier;
        this.type = BYTES_SUPPLIER;
    }

    /**
     * <p>Constructor for ZipDataSource.</p>
     *
     * @param inputStream a {@link java.io.InputStream} object
     */
    protected ZipDataSource(InputStream inputStream) {
        this.inputStream = inputStream;
        this.type = INPUT_STREAM;
    }

    /**
     * <p>Constructor for ZipDataSource.</p>
     */
    protected ZipDataSource() {
        this.type = NULL;
    }

    /**
     * <p>writeToOutputStream.</p>
     *
     * @param out a {@link java.io.OutputStream} object
     * @throws java.io.IOException if any.
     */
    public void writeToOutputStream(OutputStream out) throws IOException {
        switch (type) {
            case NULL -> {
            }
            case PATH -> Files.copy(path, out);
            case BYTES -> out.write(bytes);
            case BYTES_SUPPLIER -> out.write(bytesSupplier.get());
            case INPUT_STREAM -> inputStream.transferTo(out);
        }
    }

    /**
     * <p>toInputStream.</p>
     *
     * @return a {@link java.io.InputStream} object
     * @throws java.io.IOException if any.
     */
    public InputStream toInputStream() throws IOException {
        return switch (type) {
            case NULL -> InputStream.nullInputStream();
            case PATH -> Files.newInputStream(path);
            case BYTES -> new ByteArrayInputStream(bytes);
            case BYTES_SUPPLIER -> new ByteArrayInputStream(bytesSupplier.get());
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
