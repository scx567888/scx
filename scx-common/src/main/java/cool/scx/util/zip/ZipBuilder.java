package cool.scx.util.zip;

import cool.scx.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.zip.ZipOutputStream;

import static cool.scx.util.URIBuilder.normalize;

/**
 * 简化 zip 的创建
 *
 * @author scx567888
 * @version 0.1.4
 */
public class ZipBuilder {

    private final List<ZipBuilderItem> items = new ArrayList<>();

    /**
     * <p>Constructor for ZipBuilder.</p>
     */
    public ZipBuilder() {

    }

    public ZipBuilder(Path path) {
        this.put(path);
    }

    /**
     * <p>Constructor for ZipBuilder.</p>
     *
     * @param path       a {@link Path} object
     * @param zipOptions a {@link ZipOptions} object
     */
    public ZipBuilder(Path path, ZipOptions zipOptions) {
        this.put(path, zipOptions);
    }

    /**
     * <p>put.</p>
     *
     * @param zipPath a {@link String} object
     * @return a
     */
    public ZipBuilder put(String zipPath) {
        items.add(new ZipBuilderItem(zipPath));
        return this;
    }

    /**
     * <p>put.</p>
     *
     * @param path       a {@link String} object
     * @param zipOptions a {@link java.io.File} object
     * @return a
     */
    public ZipBuilder put(Path path, ZipOptions zipOptions) {
        items.add(new ZipBuilderItem("", path, zipOptions));
        return this;
    }

    public ZipBuilder put(Path path) {
        return this.put(path, new ZipOptions());
    }

    /**
     * <p>put.</p>
     *
     * @param path       a {@link String} object
     * @param zipPath    a {@link java.io.File} object
     * @param zipOptions a {@link ZipOptions} object
     * @return a
     */
    public ZipBuilder put(String zipPath, Path path, ZipOptions zipOptions) {
        items.add(new ZipBuilderItem(zipPath, path, zipOptions));
        return this;
    }

    /**
     * <p>put.</p>
     *
     * @param zipPath a {@link String} object
     * @param bytes   an array of {@link byte} objects
     * @return a
     */
    public ZipBuilder put(String zipPath, byte[] bytes) {
        items.add(new ZipBuilderItem(zipPath, bytes));
        return this;
    }

    /**
     * <p>put.</p>
     *
     * @param zipPath       a {@link String} object
     * @param bytesSupplier a {@link Supplier} object
     * @return a {@link ZipBuilder} object
     */
    public ZipBuilder put(String zipPath, Supplier<byte[]> bytesSupplier) {
        items.add(new ZipBuilderItem(zipPath, bytesSupplier));
        return this;
    }

    /**
     * <p>put.</p>
     *
     * @param zipPath     a {@link String} object
     * @param inputStream a {@link InputStream} object
     * @return a {@link ZipBuilder} object
     */
    public ZipBuilder put(String zipPath, InputStream inputStream) {
        items.add(new ZipBuilderItem(zipPath, inputStream));
        return this;
    }

    /**
     * <p>remove.</p>
     *
     * @param zipPath a {@link String} object
     * @return a
     */
    public ZipBuilder remove(String zipPath) {
        var p = normalize(zipPath);
        if (StringUtils.notBlank(zipPath)) {
            items.removeIf(c -> c.zipPath.startsWith(p));
        }
        return this;
    }

    /**
     * <p>writeToZipOutputStream.</p>
     *
     * @param zos a {@link ZipOutputStream} object
     * @throws IOException if any.
     */
    public void writeToZipOutputStream(ZipOutputStream zos) throws IOException {
        for (var i : items) {
            i.writeToZipOutputStream(zos);
        }
    }

    public byte[] toBytes(ZipOptions zipOptions) throws Exception {
        var bo = new ByteArrayOutputStream();
        try (var zos = new ZipOutputStream(bo, zipOptions.charset())) {
            zos.setComment(zipOptions.comment());
            zos.setLevel(zipOptions.level());
            //遍历目录
            writeToZipOutputStream(zos);
        }
        return bo.toByteArray();
    }

    /**
     * 将 virtualFile 转换为 byte 数组 方便前台用户下载使用
     *
     * @return a
     * @throws Exception a
     */
    public byte[] toBytes() throws Exception {
        return toBytes(new ZipOptions());
    }

    /**
     * 将一个虚拟文件压缩
     *
     * @param outputPath a
     * @return a
     * @throws IOException if any.
     */
    public Path toFile(Path outputPath, ZipOptions zipOptions) throws IOException {
        // 创建一个新的空的输出文件的临时文件
        Files.createDirectories(outputPath.getParent());
        try (var zos = new ZipOutputStream(Files.newOutputStream(outputPath), zipOptions.charset())) {
            zos.setComment(zipOptions.comment());
            zos.setLevel(zipOptions.level());
            writeToZipOutputStream(zos);
        }
        return outputPath;
    }

    public Path toFile(Path outputPath) throws IOException {
        return toFile(outputPath, new ZipOptions());
    }

}
