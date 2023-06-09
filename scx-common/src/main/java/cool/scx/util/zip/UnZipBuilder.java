package cool.scx.util.zip;

import cool.scx.util.FileUtils;
import cool.scx.util.zip.zip_data_source.ZipDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;
import java.util.zip.ZipInputStream;

import static cool.scx.util.URIBuilder.addSlashEnd;

/**
 * <p>UnZipBuilder class.</p>
 *
 * @author scx567888
 * @version 2.0.4
 */
public class UnZipBuilder {

    private final ZipDataSource zipDataSource;

    private Path path = null;

    public UnZipBuilder(ZipDataSource zipDataSource) {
        this.zipDataSource = zipDataSource;
    }

    public UnZipBuilder(Path path) {
        this(ZipDataSource.of(path));
        this.path = path;
    }

    public UnZipBuilder(byte[] bytes) {
        this(ZipDataSource.of(bytes));
    }

    public UnZipBuilder(Supplier<byte[]> bytesSupplier) {
        this(ZipDataSource.of(bytesSupplier));
    }

    public UnZipBuilder(InputStream inputStream) {
        this(ZipDataSource.of(inputStream));
    }

    /**
     * 解压
     *
     * @param outputPath 解压到的目录
     * @param zipOptions a
     * @throws IOException a
     */
    public void toFile(Path outputPath, ZipOptions zipOptions) throws IOException {
        Files.createDirectories(outputPath);
        var rootPath = getRootPath(zipOptions);
        try (var zis = new ZipInputStream(this.zipDataSource.toInputStream(), zipOptions.charset())) {
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

    public void toFile(Path outputPath) throws IOException {
        toFile(outputPath, new ZipOptions());
    }

    /**
     * <p>getRootPath.</p>
     *
     * @param zipOptions a {@link ZipOptions} object
     * @return a {@link String} object
     */
    private String getRootPath(ZipOptions zipOptions) {
        if (zipOptions.includeRoot() && this.path != null) {
            var fileName = this.path.getFileName().toString();
            var fileNameWithoutExtension = FileUtils.getFileNameWithoutExtension(fileName);
            return addSlashEnd(fileNameWithoutExtension);
        }
        return "";
    }

}
