package cool.scx.util.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * a
 *
 * @author scx567888
 * @version 2.0.4
 */
public class ZipBuilderItem implements ZipDataSource {

    /**
     * 经过处理后必须为 "" 或者 结尾是 "/", 开头不允许有 "/"
     */
    protected final String zipPath;

    protected final ZipDataSource zipDataSource;

    protected ZipBuilderItem(String zipPath, ZipDataSource zipDataSource) {
        this.zipPath = zipPath;
        this.zipDataSource = zipDataSource;
    }

    public void writeToZipOutputStream(ZipOutputStream zos) throws IOException {
        zos.putNextEntry(new ZipEntry(zipPath));
        writeToOutputStream(zos);
        zos.closeEntry();
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        this.zipDataSource.writeToOutputStream(out);
    }

    @Override
    public InputStream toInputStream() throws IOException {
        return this.zipDataSource.toInputStream();
    }

}
