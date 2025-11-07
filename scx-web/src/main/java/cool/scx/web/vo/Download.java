package cool.scx.web.vo;

import java.io.File;
import java.io.InputStream;

import static cool.scx.web.HttpHelper.getDownloadContentDisposition;
import static cool.scx.web.HttpHelper.getMediaTypeByFileName;

/// 文件下载 vo
///
/// @author scx567888
/// @version 0.0.1
public final class Download extends BaseWriter {

    private Download(InputStream inputStream, String downloadName) {
        super(inputStream, getMediaTypeByFileName(downloadName), getDownloadContentDisposition(downloadName));
    }

    private Download(File file, String downloadName) {
        super(file, getMediaTypeByFileName(downloadName), getDownloadContentDisposition(downloadName));
    }

    private Download(byte[] bytes, String downloadName) {
        super(bytes, getMediaTypeByFileName(downloadName), getDownloadContentDisposition(downloadName));
    }

    public static Download of(InputStream inputStream, String downloadName) {
        return new Download(inputStream, downloadName);
    }

    public static Download of(byte[] bytes, String downloadName) {
        return new Download(bytes, downloadName);
    }

    public static Download of(File file, String downloadName) {
        return new Download(file, downloadName);
    }

    public static Download of(File file) {
        return new Download(file, file.getName());
    }

}
