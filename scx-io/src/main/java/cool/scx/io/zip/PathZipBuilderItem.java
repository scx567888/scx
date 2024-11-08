package cool.scx.io.zip;

import cool.scx.common.util.StringUtils;
import cool.scx.common.util.URIBuilder;
import cool.scx.io.input_source.FileInputSource;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class PathZipBuilderItem extends ZipBuilderItem {

    private final Path path;

    public PathZipBuilderItem(String zipPath, Path path, ZipOptions zipOptions) {
        super(getZipPathByPath(zipPath, path, zipOptions), new FileInputSource(path));
        this.path = path;
    }

    private static String getZipPathByPath(String zipPath, Path path, ZipOptions zipOptions) {
        var fileName = path.getFileName().toString();
        var normalizeZipPath = URIBuilder.trimSlash(URIBuilder.normalize(zipPath));

        if (Files.isDirectory(path)) {//文件夹特殊处理 根据 info 判断是否拼接 文件夹名称
            var dirName = zipOptions.includeRoot() ? fileName : "";
            var rootPath = URIBuilder.join(normalizeZipPath, dirName); //判断是否有自定义目录并进行拼接
            return StringUtils.isEmpty(rootPath) ? rootPath : URIBuilder.addSlashEnd(rootPath);
        } else {
            if (StringUtils.isEmpty(normalizeZipPath)) { //zipPath 为空时, 我们直接将文件名设置为 zipPath
                return fileName;
            } else if (zipOptions.useOriginalFileName()) {
                return URIBuilder.join(normalizeZipPath, fileName);
            } else {
                return normalizeZipPath;
            }
        }
    }

    @Override
    public void writeToZipOutputStream(ZipOutputStream zos) throws IOException {
        //文件夹做特殊处理
        if (Files.isDirectory(path)) {
            writeToZipOutputStreamByDirectory(zos);
        } else {
            super.writeToZipOutputStream(zos);
        }
    }

    private void writeToZipOutputStreamByDirectory(ZipOutputStream zos) throws IOException {
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
