package cool.scx.mvc.vo;

import cool.scx.common.standard.MediaType;
import cool.scx.common.util.HttpUtils;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * 文件下载 vo
 *
 * @author scx567888
 * @version 1.0.10
 */
public final class Download extends BaseWriter {

    /**
     * <p>Constructor for Download.</p>
     *
     * @param inputStream  a {@link java.io.InputStream} object
     * @param downloadName a {@link java.lang.String} object
     */
    private Download(InputStream inputStream, String downloadName) {
        super(inputStream, MediaType.ofFileName(downloadName), HttpUtils.getDownloadContentDisposition(downloadName));
    }

    /**
     * <p>Constructor for Download.</p>
     *
     * @param path         a {@link java.nio.file.Path} object
     * @param downloadName a {@link java.lang.String} object
     */
    private Download(Path path, String downloadName) {
        super(path, MediaType.ofFileName(downloadName), HttpUtils.getDownloadContentDisposition(downloadName));
    }

    /**
     * <p>Constructor for Download.</p>
     *
     * @param bytes        an array of {@link byte} objects
     * @param downloadName a {@link java.lang.String} object
     */
    private Download(byte[] bytes, String downloadName) {
        super(bytes, MediaType.ofFileName(downloadName), HttpUtils.getDownloadContentDisposition(downloadName));
    }

    /**
     * a
     *
     * @param inputStream  a
     * @param downloadName a
     * @return a
     */
    public static Download of(InputStream inputStream, String downloadName) {
        return new Download(inputStream, downloadName);
    }

    /**
     * a
     *
     * @param bytes        a
     * @param downloadName a
     * @return a
     */
    public static Download of(byte[] bytes, String downloadName) {
        return new Download(bytes, downloadName);
    }

    /**
     * a
     *
     * @param path         a
     * @param downloadName a
     * @return a
     */
    public static Download of(Path path, String downloadName) {
        return new Download(path, downloadName);
    }

    /**
     * a
     *
     * @param path a
     * @return a
     */
    public static Download of(Path path) {
        return new Download(path, path.getFileName().toString());
    }

}
