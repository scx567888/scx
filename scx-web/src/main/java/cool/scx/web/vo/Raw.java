package cool.scx.web.vo;

import cool.scx.http.media_type.FileFormat;

import java.io.File;
import java.io.InputStream;

/// 原始文件 但不需要下载的 vo 比如 pdf 之类
///
/// @author scx567888
/// @version 0.0.1
public final class Raw extends BaseWriter {

    private Raw(InputStream inputStream, FileFormat fileFormat) {
        super(inputStream, fileFormat.mediaType(), "inline");
    }

    private Raw(File file) {
        super(file, null, "inline");
    }

    private Raw(byte[] bytes, FileFormat fileFormat) {
        super(bytes, fileFormat.mediaType(), "inline");
    }

    public static Raw of(byte[] bytes, FileFormat fileFormat) {
        return new Raw(bytes, fileFormat);
    }

    public static Raw of(File file) {
        return new Raw(file);
    }

    public static Raw of(InputStream inputStream, FileFormat fileFormat) {
        return new Raw(inputStream, fileFormat);
    }

}
