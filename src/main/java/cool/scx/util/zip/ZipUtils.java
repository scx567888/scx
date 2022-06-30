package cool.scx.util.zip;

import cool.scx.util.StringUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * zip 压缩器
 *
 * @author scx567888
 * @version 1.7.3
 */
public final class ZipUtils {

    /**
     * 将文件夹压缩成压缩文件
     *
     * @param sourcePath 源文件路径
     * @param outputPath zip 输出路径
     * @throws java.io.IOException io
     */
    public static void zipFile(Path sourcePath, Path outputPath) throws IOException {
        // 获取源路径 path 用于和后续文件进行相对路径的计算
        // 创建一个新的空的输出文件的临时文件
        Files.createDirectories(outputPath.getParent());
        try (var zos = new ZipOutputStream(Files.newOutputStream(outputPath))) {
            //遍历目录
            Files.walkFileTree(sourcePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    var zipEntryPath = sourcePath.relativize(dir).toString();
                    if (StringUtils.notBlank(zipEntryPath)) {
                        if (attrs.isDirectory()) {
                            var zipEntry = new ZipEntry(zipEntryPath + "/");
                            zos.putNextEntry(zipEntry);
                            zos.closeEntry();
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    var zipEntryPath = sourcePath.relativize(file).toString();
                    if (StringUtils.notBlank(zipEntryPath)) {
                        ZipEntry zipEntry;
                        if (attrs.isDirectory()) {
                            zipEntry = new ZipEntry(zipEntryPath + "/");
                            zos.putNextEntry(zipEntry);
                        } else {
                            zipEntry = new ZipEntry(zipEntryPath);
                            zos.putNextEntry(zipEntry);
                            Files.copy(file, zos);
                        }
                        zos.closeEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }

            });
        }
    }

}
