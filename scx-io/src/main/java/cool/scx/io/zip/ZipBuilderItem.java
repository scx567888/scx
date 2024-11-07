package cool.scx.io.zip;

import cool.scx.common.util.URIBuilder;
import cool.scx.io.io_source.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * ZipBuilderItem
 *
 * @author scx567888
 * @version 2.0.4
 */
public class ZipBuilderItem {

    /**
     * 经过处理后必须为 "" 或者 结尾是 "/", 开头不允许有 "/"
     */
    protected final String zipPath;

    protected final InputSource source;

    protected ZipBuilderItem(String zipPath, InputSource source) {
        this.zipPath = zipPath;
        this.source = source;
    }

    ZipBuilderItem(String zipPath, byte[] bytes) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipPath));
        this.source = InputSource.of(bytes);
    }

    ZipBuilderItem(String zipPath, Supplier<byte[]> bytesSupplier) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipPath));
        this.source = InputSource.of(bytesSupplier);
    }

    ZipBuilderItem(String zipPath, InputStream inputStream) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipPath));
        this.source = InputSource.of(inputStream);
    }

    ZipBuilderItem(String zipPath) {
        this.zipPath = URIBuilder.addSlashEnd(URIBuilder.trimSlash(URIBuilder.normalize(zipPath)));
        this.source = InputSource.of();
    }

    ZipBuilderItem(ZipEntry zipEntry, ZipFile zipFile) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipEntry.getName()));
        this.source = InputSource.of(zipEntry, zipFile);
    }

    public void writeToZipOutputStream(ZipOutputStream zos) throws IOException {
        zos.putNextEntry(new ZipEntry(this.zipPath));
        this.source.writeToOutputStream(zos);
        zos.closeEntry();
    }

    public InputSource zipDataSource() {
        return source;
    }

}
