package cool.scx.common.util.zip;

import cool.scx.common.util.URIBuilder;
import cool.scx.common.util.io_stream_source.InputStreamSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * a
 *
 * @author scx567888
 * @version 2.0.4
 */
public class ZipBuilderItem {

    /**
     * 经过处理后必须为 "" 或者 结尾是 "/", 开头不允许有 "/"
     */
    protected final String zipPath;

    protected final InputStreamSource source;

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath a {@link java.lang.String} object
     */
    protected ZipBuilderItem(String zipPath, InputStreamSource source) {
        this.zipPath = zipPath;
        this.source = source;
    }

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath a {@link java.lang.String} object
     * @param bytes   an array of {@link byte} objects
     */
    ZipBuilderItem(String zipPath, byte[] bytes) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipPath));
        this.source = InputStreamSource.of(bytes);
    }

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath       a {@link java.lang.String} object
     * @param bytesSupplier a {@link java.util.function.Supplier} object
     */
    ZipBuilderItem(String zipPath, Supplier<byte[]> bytesSupplier) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipPath));
        this.source = InputStreamSource.of(bytesSupplier);
    }

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath     a {@link java.lang.String} object
     * @param inputStream a {@link java.io.InputStream} object
     */
    ZipBuilderItem(String zipPath, InputStream inputStream) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipPath));
        this.source = InputStreamSource.of(inputStream);
    }

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath a {@link java.lang.String} object
     */
    ZipBuilderItem(String zipPath) {
        this.zipPath = URIBuilder.addSlashEnd(URIBuilder.trimSlash(URIBuilder.normalize(zipPath)));
        this.source = InputStreamSource.of();
    }

    ZipBuilderItem(ZipEntry zipEntry, ZipFile zipFile) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipEntry.getName()));
        this.source = InputStreamSource.of(zipEntry, zipFile);
    }

    public void writeToZipOutputStream(ZipOutputStream zos) throws IOException {
        zos.putNextEntry(new ZipEntry(this.zipPath));
        this.source.writeToOutputStream(zos);
        zos.closeEntry();
    }

    public InputStreamSource zipDataSource() {
        return source;
    }

}
