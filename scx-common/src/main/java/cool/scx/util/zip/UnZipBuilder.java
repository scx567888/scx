package cool.scx.util.zip;

import cool.scx.util.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;
import java.util.zip.ZipInputStream;

import static cool.scx.util.URIBuilder.addSlashEnd;
import static cool.scx.util.zip.ZipDataSource.Type.PATH;

public class UnZipBuilder extends ZipDataSource {

    public UnZipBuilder(Path path) {
        super(path);
    }

    public UnZipBuilder(byte[] bytes) {
        super(bytes);
    }

    public UnZipBuilder(Supplier<byte[]> bytesSupplier) {
        super(bytesSupplier);
    }

    public UnZipBuilder(InputStream inputStream) {
        super(inputStream);
    }

    /**
     * 解压
     *
     * @param outputPath 解压到的目录
     * @param zipOptions a
     * @throws java.io.IOException a
     */
    public void toFile(Path outputPath, ZipOption... zipOptions) throws IOException {
        Files.createDirectories(outputPath);
        var rootPath = getRootPath(zipOptions);
        try (var zis = new ZipInputStream(toInputStream())) {
            // 遍历每一个文件
            var zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                var unzipFilePath = outputPath.resolve(rootPath + zipEntry.getName());
                if (zipEntry.isDirectory()) { // 文件夹
                    Files.createDirectories(unzipFilePath);
                } else { // 文件
                    Files.createDirectories(unzipFilePath.getParent());
                    Files.copy(zis, unzipFilePath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
        }
    }

    public String getRootPath(ZipOption... zipOptions) {
        var info = new ZipOption.Info(zipOptions);
        var fileName = path.getFileName().toString();
        var fileNameWithoutExtension = FileUtils.getFileNameWithoutExtension(fileName);
        return this.type == PATH && info.includeRoot() ? addSlashEnd(fileNameWithoutExtension) : "";
    }

}
