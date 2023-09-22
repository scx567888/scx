package cool.scx.mvc.vo;

import cool.scx.enumeration.FileFormat;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * 原始文件 但不需要下载的 vo
 * 比如 pdf 之类
 *
 * @author scx567888
 * @version 0.7.0
 */
public final class Raw extends BaseWriter {

    /**
     * <p>Constructor for Raw.</p>
     *
     * @param inputStream a {@link java.io.InputStream} object
     * @param fileFormat  a {@link FileFormat} object
     */
    private Raw(InputStream inputStream, FileFormat fileFormat) {
        super(inputStream, fileFormat.mimeType(), "inline");
    }

    /**
     * <p>Constructor for Raw.</p>
     *
     * @param path a {@link java.nio.file.Path} object
     */
    private Raw(Path path) {
        super(path, null, "inline");
    }

    /**
     * <p>Constructor for Raw.</p>
     *
     * @param bytes      an array of {@link byte} objects
     * @param fileFormat a {@link FileFormat} object
     */
    private Raw(byte[] bytes, FileFormat fileFormat) {
        super(bytes, fileFormat.mimeType(), "inline");
    }

    /**
     * a
     *
     * @param bytes      a
     * @param fileFormat a
     * @return a
     */
    public static Raw of(byte[] bytes, FileFormat fileFormat) {
        return new Raw(bytes, fileFormat);
    }

    /**
     * a
     *
     * @param path a
     * @return a
     */
    public static Raw of(Path path) {
        return new Raw(path);
    }

    /**
     * a
     *
     * @param inputStream a
     * @param fileFormat  a
     * @return a
     */
    public static Raw of(InputStream inputStream, FileFormat fileFormat) {
        return new Raw(inputStream, fileFormat);
    }

}
