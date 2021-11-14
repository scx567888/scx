package cool.scx.util.zip;

import cool.scx.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
public final class ZipAction {

    /**
     * 将 virtualFile 转换为 byte 数组 方便前台用户下载使用
     *
     * @param virtualFile v
     * @return a
     * @throws java.lang.Exception a
     */
    public static byte[] toZipFileByteArray(IVirtualFile virtualFile) throws Exception {
        var bo = new ByteArrayOutputStream();
        try (var zos = new ZipOutputStream(bo)) {
            //遍历目录
            writeVirtualFileToZipOutputStream(virtualFile, zos);
        }
        return bo.toByteArray();
    }

    /**
     * 将一个虚拟文件压缩
     *
     * @param virtualFile a
     * @param outputFile  a
     * @throws java.lang.Exception a
     */
    public static void zipFile(IVirtualFile virtualFile, String outputFile) throws Exception {
        // 创建一个新的空的输出文件的临时文件
        var outputFilePath = Files.createFile(Path.of(outputFile));
        try (var zos = new ZipOutputStream(Files.newOutputStream(outputFilePath))) {
            //遍历目录
            writeVirtualFileToZipOutputStream(virtualFile, zos);
        }
    }

    /**
     * 将虚拟文件写入到 zip 流
     *
     * @param virtualFile a
     * @param zos         a
     * @throws java.lang.Exception a
     */
    private static void writeVirtualFileToZipOutputStream(IVirtualFile virtualFile, ZipOutputStream zos) throws Exception {
        walk(virtualFile, new VirtualFileVisitor() {
            @Override
            public void visitDirectory(String path) throws IOException {
                var zipEntry = new ZipEntry(path);
                zos.putNextEntry(zipEntry);
                zos.closeEntry();
            }

            @Override
            public void visitFile(String path, byte[] bytes) throws IOException {
                var zipEntry = new ZipEntry(path);
                zos.putNextEntry(zipEntry);
                zos.write(bytes);
                zos.closeEntry();
            }

        });
    }

    /**
     * 将文件夹压缩成压缩文件
     *
     * @param sourceFile 源文件路径
     * @param outputFile zip 输出路径
     * @throws java.io.IOException io
     */
    public static void zipFile(File sourceFile, String outputFile) throws IOException {
        // 获取源路径 path 用于和后续文件进行相对路径的计算
        var sourceFilePath = sourceFile.toPath();
        // 创建一个新的空的输出文件的临时文件
        var outputFilePath = Files.createFile(Path.of(outputFile));
        try (var zos = new ZipOutputStream(Files.newOutputStream(outputFilePath))) {
            //遍历目录
            Files.walkFileTree(sourceFilePath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    var zipEntryPath = sourceFilePath.relativize(dir).toString();
                    if (StringUtils.isNotBlank(zipEntryPath)) {
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
                    var zipEntryPath = sourceFilePath.relativize(file).toString();
                    if (StringUtils.isNotBlank(zipEntryPath)) {
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

    /**
     * 循环遍历一个 虚拟文件
     *
     * @param iVirtualFile a
     * @param visitor      a
     * @throws java.lang.Exception a
     */
    public static void walk(IVirtualFile iVirtualFile, VirtualFileVisitor visitor) throws Exception {
        recursionWalk(null, iVirtualFile, visitor);
    }

    /**
     * 递归 walk
     *
     * @param parentPath   父目录
     * @param iVirtualFile i
     * @param visitor      a
     * @throws java.lang.Exception e
     */
    private static void recursionWalk(String parentPath, IVirtualFile iVirtualFile, VirtualFileVisitor visitor) throws Exception {
        var fullPath = parentPath == null ? iVirtualFile.path() : parentPath + "/" + iVirtualFile.path();
        if (iVirtualFile instanceof VirtualFile virtualFile) {
            visitor.visitFile(fullPath, virtualFile.getBytes());
        } else if (iVirtualFile instanceof VirtualDirectory virtualDirectory) {
            if (fullPath != null) {
                visitor.visitDirectory(fullPath + "/");
            }
            for (var child : virtualDirectory.children()) {
                recursionWalk(fullPath, child, visitor);
            }
        }
    }

}
