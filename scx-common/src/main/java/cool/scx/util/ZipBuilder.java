package cool.scx.util;

import cool.scx.functional.ScxHandlerR;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cool.scx.util.URIBuilder.*;

/**
 * 简化 zip 的创建
 *
 * @author scx567888
 * @version 0.1.4
 */
public class ZipBuilder {

    private final List<ZipItem> items = new ArrayList<>();

    /**
     * <p>put.</p>
     *
     * @param path a {@link java.lang.String} object
     * @return a
     */
    public ZipBuilder put(String path) {
        items.add(new ZipItem(path));
        return this;
    }

    /**
     * <p>put.</p>
     *
     * @param path a {@link java.lang.String} object
     * @param f    a {@link java.io.File} object
     * @return a
     */
    public ZipBuilder put(String path, File f) {
        items.add(new ZipItem(path, f));
        return this;
    }

    /**
     * <p>put.</p>
     *
     * @param path  a {@link java.lang.String} object
     * @param bytes an array of {@link byte} objects
     * @return a
     */
    public ZipBuilder put(String path, byte[] bytes) {
        items.add(new ZipItem(path, bytes));
        return this;
    }

    /**
     * <p>put.</p>
     *
     * @param path          a {@link java.lang.String} object
     * @param bytesSupplier a {@link cool.scx.functional.ScxHandlerR} object
     * @return a {@link cool.scx.util.ZipBuilder} object
     */
    public ZipBuilder put(String path, ScxHandlerR<byte[]> bytesSupplier) {
        items.add(new ZipItem(path, bytesSupplier));
        return this;
    }

    /**
     * <p>put.</p>
     *
     * @param path        a {@link java.lang.String} object
     * @param inputStream a {@link java.io.InputStream} object
     * @return a {@link cool.scx.util.ZipBuilder} object
     */
    public ZipBuilder put(String path, InputStream inputStream) {
        items.add(new ZipItem(path, inputStream));
        return this;
    }

    /**
     * <p>remove.</p>
     *
     * @param path a {@link java.lang.String} object
     * @return a
     */
    public ZipBuilder remove(String path) {
        var p = normalize(path);
        if (StringUtils.notBlank(path)) {
            items.removeIf(c -> c.path.startsWith(p));
        }
        return this;
    }

    /**
     * <p>writeToZipOutputStream.</p>
     *
     * @param zos a {@link java.util.zip.ZipOutputStream} object
     * @throws java.lang.Exception if any.
     */
    public void writeToZipOutputStream(ZipOutputStream zos) throws Exception {
        for (var i : items) {
            switch (i.type) {
                case DIR -> zos.putNextEntry(new ZipEntry(i.path));
                case FILE -> {
                    if (i.file.isDirectory()) {
                        ZipUtils.writeToZipOutputStream(i.file.toPath(), zos, i.path);
                    } else {
                        zos.putNextEntry(new ZipEntry(i.path));
                        Files.copy(i.file.toPath(), zos);
                    }
                }
                case BYTES -> {
                    zos.putNextEntry(new ZipEntry(i.path));
                    zos.write(i.bytes);
                }
                case BYTES_SUPPLIER -> {
                    zos.putNextEntry(new ZipEntry(i.path));
                    zos.write(i.bytesSupplier.handle());
                }
                case INPUT_STREAM -> {
                    zos.putNextEntry(new ZipEntry(i.path));
                    i.inputStream.transferTo(zos);
                }
            }
            zos.closeEntry();
        }
    }

    /**
     * 将 virtualFile 转换为 byte 数组 方便前台用户下载使用
     *
     * @return a
     * @throws java.lang.Exception a
     */
    public byte[] toZipBytes() throws Exception {
        var bo = new ByteArrayOutputStream();
        try (var zos = new ZipOutputStream(bo)) {
            //遍历目录
            writeToZipOutputStream(zos);
        }
        return bo.toByteArray();
    }

    /**
     * 将一个虚拟文件压缩
     *
     * @param outputPath a
     * @throws java.lang.Exception a
     */
    public void toZipFile(Path outputPath) throws Exception {
        // 创建一个新的空的输出文件的临时文件
        Files.createDirectories(outputPath.getParent());
        try (var zos = new ZipOutputStream(Files.newOutputStream(outputPath))) {
            //遍历目录
            writeToZipOutputStream(zos);
        }
    }

    enum ZipItemType {
        DIR,
        FILE,
        BYTES,
        BYTES_SUPPLIER,
        INPUT_STREAM,
    }

    /**
     * a
     */
    static final class ZipItem {
        private final String path;
        private final ZipItemType type;
        private File file;
        private byte[] bytes;
        private ScxHandlerR<byte[]> bytesSupplier;
        private InputStream inputStream;

        /**
         * @param path 总路径 包括自己的名称
         */
        ZipItem(String path, File file) {
            this.path = trimSlash(normalize(path));
            this.file = file;
            this.type = ZipItemType.FILE;
        }

        ZipItem(String path, byte[] bytes) {
            this.path = trimSlash(normalize(path));
            this.bytes = bytes;
            this.type = ZipItemType.BYTES;
        }

        ZipItem(String path, ScxHandlerR<byte[]> bytesSupplier) {
            this.path = trimSlash(normalize(path));
            this.bytesSupplier = bytesSupplier;
            this.type = ZipItemType.BYTES_SUPPLIER;
        }

        ZipItem(String path, InputStream inputStream) {
            this.path = trimSlash(normalize(path));
            this.inputStream = inputStream;
            this.type = ZipItemType.INPUT_STREAM;
        }

        ZipItem(String path) {
            this.path = addSlashEnd(trimSlash(normalize(path)));
            this.type = ZipItemType.DIR;
        }

    }

}
