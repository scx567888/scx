package cool.scx.core.vo;

import cool.scx.core.enumeration.RawType;
import io.vertx.core.http.impl.MimeMapping;

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

    private Raw(InputStream inputStream, RawType rawType) {
        super(inputStream, MimeMapping.getMimeTypeForExtension(rawType.name().toLowerCase()), "inline");
    }

    private Raw(Path path) {
        super(path, null, "inline");
    }

    private Raw(byte[] bytes, RawType rawType) {
        super(bytes, MimeMapping.getMimeTypeForExtension(rawType.name().toLowerCase()), "inline");
    }

    /**
     * a
     *
     * @param bytes   a
     * @param rawType a
     * @return a
     */
    public static Raw of(byte[] bytes, RawType rawType) {
        return new Raw(bytes, rawType);
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
     * @param rawType     a
     * @return a
     */
    public static Raw of(InputStream inputStream, RawType rawType) {
        return new Raw(inputStream, rawType);
    }

}
