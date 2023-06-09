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

/**
 * <p>UnZipBuilder class.</p>
 *
 * @author scx567888
 * @version 2.0.4
 */
public class UnZipBuilder extends ZipDataSource {

    /**
     * <p>Constructor for UnZipBuilder.</p>
     *
     * @param path a {@link java.nio.file.Path} object
     */
    public UnZipBuilder(Path path) {
        super(path);
    }

    /**
     * <p>Constructor for UnZipBuilder.</p>
     *
     * @param bytes an array of {@link byte} objects
     */
    public UnZipBuilder(byte[] bytes) {
        super(bytes);
    }

    /**
     * <p>Constructor for UnZipBuilder.</p>
     *
     * @param bytesSupplier a {@link java.util.function.Supplier} object
     */
    public UnZipBuilder(Supplier<byte[]> bytesSupplier) {
        super(bytesSupplier);
    }

    /**
     * <p>Constructor for UnZipBuilder.</p>
     *
     * @param inputStream a {@link java.io.InputStream} object
     */
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
    public void toFile(Path outputPath, ZipOptions zipOptions) throws IOException {
        Files.createDirectories(outputPath);
        var rootPath = getRootPath(zipOptions);
        try (var zis = new ZipInputStream(toInputStream(), zipOptions.charset())) {
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
     * @param zipOptions a {@link cool.scx.util.zip.ZipOptions} object
     * @return a {@link java.lang.String} object
     */
    private String getRootPath(ZipOptions zipOptions) {
        if (this.type == PATH && zipOptions.includeRoot()) {
            var fileName = path.getFileName().toString();
            var fileNameWithoutExtension = FileUtils.getFileNameWithoutExtension(fileName);
            return addSlashEnd(fileNameWithoutExtension);
        }
        return "";
    }

}
