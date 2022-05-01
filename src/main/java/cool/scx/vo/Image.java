package cool.scx.vo;

import cool.scx.http.exception.impl.BadRequestException;
import cool.scx.http.exception.impl.NotFoundException;
import cool.scx.vo.impl.CroppedImage;
import cool.scx.vo.impl.OriginalImage;
import cool.scx.vo.impl.SystemIconImage;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;

import java.io.File;
import java.util.Objects;

/**
 * <p>Image class.</p>
 *
 * @author scx567888
 * @version 1.0.10
 */
public abstract class Image implements BaseVo {

    public static Image of(File file) {
        return of(file, null, null, null);
    }

    public static Image of(File file, Integer width, Integer height, String type) {
        var contentType = MimeMapping.getMimeTypeForFilename(file.getName());
        if (contentType != null && contentType.startsWith("image")) {
            if (height == null && width == null) {
                return new OriginalImage(file);
            } else {
                return new CroppedImage(file, width, height, type);
            }
        } else {
            return new SystemIconImage(file);
        }
    }

    /**
     * 用来校验 file 是否可用
     *
     * @param _file a {@link java.io.File} object.
     */
    protected Image(File _file) {
        Objects.requireNonNull(_file, "图片文件不能为 null");
        // 图片不存在 这里抛出不存在异常
        if (!_file.exists()) {
            throw new NotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * sendToClient
     */
    @Override
    public final void handle(RoutingContext context) throws BadRequestException {
        //设置缓存 减少服务器压力
        context.response()
                .putHeader(HttpHeaderNames.CACHE_CONTROL, "public,immutable,max-age=2628000")
                .putHeader(HttpHeaderNames.ACCEPT_RANGES, "bytes");
        imageHandler(context);
    }

    public abstract void imageHandler(RoutingContext context);

}
