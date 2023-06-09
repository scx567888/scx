package cool.scx.util.zip;

import cool.scx.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cool.scx.util.URIBuilder.*;
import static cool.scx.util.zip.ZipDataSource.Type.PATH;

/**
 * a
 *
 * @author scx567888
 * @version 2.0.4
 */
final class ZipBuilderItem extends ZipDataSource {

    /**
     * 经过处理后必须为 "" 或者 结尾是 "/", 开头不允许有 "/"
     */
    final String zipPath;

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath    a {@link java.lang.String} object
     * @param path       a {@link java.nio.file.Path} object
     * @param zipOptions a {@link cool.scx.util.zip.ZipOptions} object
     */
    ZipBuilderItem(String zipPath, Path path, ZipOptions zipOptions) {
        super(path);
        this.zipPath = getZipPathByPath(zipPath, path, zipOptions);
    }

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath a {@link java.lang.String} object
     * @param bytes   an array of {@link byte} objects
     */
    ZipBuilderItem(String zipPath, byte[] bytes) {
        super(bytes);
        this.zipPath = trimSlash(normalize(zipPath));
    }

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath       a {@link java.lang.String} object
     * @param bytesSupplier a {@link java.util.function.Supplier} object
     */
    ZipBuilderItem(String zipPath, Supplier<byte[]> bytesSupplier) {
        super(bytesSupplier);
        this.zipPath = trimSlash(normalize(zipPath));
    }

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath     a {@link java.lang.String} object
     * @param inputStream a {@link java.io.InputStream} object
     */
    ZipBuilderItem(String zipPath, InputStream inputStream) {
        super(inputStream);
        this.zipPath = trimSlash(normalize(zipPath));
    }

    /**
     * <p>Constructor for ZipBuilderItem.</p>
     *
     * @param zipPath a {@link java.lang.String} object
     */
    ZipBuilderItem(String zipPath) {
        super();
        this.zipPath = addSlashEnd(trimSlash(normalize(zipPath)));
    }

    /**
     * <p>getZipPathByPath.</p>
     *
     * @param zipPath    a {@link java.lang.String} object
     * @param path       a {@link java.nio.file.Path} object
     * @param zipOptions a {@link cool.scx.util.zip.ZipOptions} object
     * @return a {@link java.lang.String} object
     */
    public static String getZipPathByPath(String zipPath, Path path, ZipOptions zipOptions) {
        var fileName = path.getFileName().toString();
        var normalizeZipPath = trimSlash(normalize(zipPath));

        if (Files.isDirectory(path)) {//文件夹特殊处理 根据 info 判断是否拼接 文件夹名称
            var dirName = zipOptions.includeRoot() ? fileName : "";
            var rootPath = join(normalizeZipPath, dirName); //判断是否有自定义目录并进行拼接
            return StringUtils.isEmpty(rootPath) ? rootPath : addSlashEnd(rootPath);
        } else {
            if (StringUtils.isEmpty(normalizeZipPath)) { //zipPath 为空时, 我们直接将文件名设置为 zipPath
                return fileName;
            } else if (zipOptions.useOriginalFileName()) {
                return join(normalizeZipPath, fileName);
            } else {
                return normalizeZipPath;
            }
        }
    }

    /**
     * <p>writeToZipOutputStream.</p>
     *
     * @param zos a {@link java.util.zip.ZipOutputStream} object
     * @throws java.io.IOException if any.
     */
    public void writeToZipOutputStream(ZipOutputStream zos) throws IOException {
        //文件夹做特殊处理
        if (type == PATH && Files.isDirectory(path)) {
            writeToZipOutputStreamByDirectory(zos);
        } else {
            zos.putNextEntry(new ZipEntry(zipPath));
            writeToOutputStream(zos);
            zos.closeEntry();
        }
    }

    /**
     * <p>writeToZipOutputStreamByDirectory.</p>
     *
     * @param zos a {@link java.util.zip.ZipOutputStream} object
     * @throws java.io.IOException if any.
     */
    public void writeToZipOutputStreamByDirectory(ZipOutputStream zos) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                var finalPath = zipPath + path.relativize(file);
                if (attrs.isDirectory()) {
                    zos.putNextEntry(new ZipEntry(finalPath + "/"));
                } else {
                    zos.putNextEntry(new ZipEntry(finalPath));
                    Files.copy(file, zos);
                }
                zos.closeEntry();
                return FileVisitResult.CONTINUE;
            }

        });
    }

}
