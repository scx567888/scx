package cool.scx.io.zip;

import cool.scx.common.util.URIBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    protected final InputStream source;

    ZipBuilderItem(String zipPath, byte[] bytes) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipPath));
        this.source = new ByteArrayInputStream(bytes);
    }

    ZipBuilderItem(String zipPath, InputStream inputStream) {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipPath));
        this.source = inputStream;
    }

    ZipBuilderItem(String zipPath) {
        this.zipPath = URIBuilder.addSlashEnd(URIBuilder.trimSlash(URIBuilder.normalize(zipPath)));
        this.source = InputStream.nullInputStream();
    }

    ZipBuilderItem(ZipEntry zipEntry, ZipFile zipFile) throws IOException {
        this.zipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipEntry.getName()));
        this.source = zipFile.getInputStream(zipEntry);
    }

    public void writeToZipOutputStream(ZipOutputStream zos) throws IOException {
        zos.putNextEntry(new ZipEntry(this.zipPath));
        this.source.transferTo(zos);
        zos.closeEntry();
    }

    public InputStream zipDataSource() {
        return source;
    }

}
