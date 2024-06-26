package cool.scx.common.zip;

import cool.scx.common.io_stream_source.InputStreamSource;
import cool.scx.common.util.FileUtils;
import cool.scx.common.util.URIBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;
import java.util.zip.ZipInputStream;

/**
 * <p>UnZipBuilder class.</p>
 *
 * @author scx567888
 * @version 2.0.4
 */
public final class UnZipBuilder {

    private final InputStreamSource source;

    private Path path = null;

    public UnZipBuilder(InputStreamSource source) {
        this.source = source;
    }

    public UnZipBuilder(Path path) {
        this(InputStreamSource.of(path));
        this.path = path;
    }

    public UnZipBuilder(byte[] bytes) {
        this(InputStreamSource.of(bytes));
    }

    public UnZipBuilder(Supplier<byte[]> bytesSupplier) {
        this(InputStreamSource.of(bytesSupplier));
    }

    public UnZipBuilder(InputStream inputStream) {
        this(InputStreamSource.of(inputStream));
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
        try (var zis = new ZipInputStream(this.source.toInputStream(), zipOptions.charset())) {
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
            return URIBuilder.addSlashEnd(fileNameWithoutExtension);
        }
        return "";
    }

}
