package cool.scx.vo;

import cool.scx.enumeration.RawType;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * 原始文件 但不需要下载的 vo
 * 比如 pdf 之类
 *
 * @author scx567888
 * @version 0.7.0
 */
public final class Raw extends VoWriter {

    /**
     * 文件类型 主要用于当数据类型是 byte 或者 inputStream 时, 确定像前台发送的 ContentType
     */
    private final RawType rawType;

    private Raw(InputStream inputStream, RawType rawType) {
        super(inputStream);
        this.rawType = rawType;
    }

    private Raw(Path path) {
        super(path);
        this.rawType = RawType.BIN;
    }

    private Raw(byte[] bytes, RawType rawType) {
        super(bytes);
        this.rawType = rawType;
    }

    public static Raw of(byte[] bytes, RawType rawType) {
        return new Raw(bytes, rawType);
    }

    public static Raw of(Path path) {
        return new Raw(path);
    }

    public static Raw of(InputStream inputStream, RawType rawType) {
        return new Raw(inputStream, rawType);
    }

    @Override
    public void sendBytes(RoutingContext context) {
        BaseVo.fillContentType(MimeMapping.getMimeTypeForExtension(rawType.name().toLowerCase()), context.request().response())
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, "inline");
        super.sendBytes(context);
    }

    @Override
    public void sendInputStream(RoutingContext context) {
        BaseVo.fillContentType(MimeMapping.getMimeTypeForExtension(rawType.name().toLowerCase()), context.request().response())
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, "inline");
        super.sendInputStream(context);
    }

}
