package cool.scx.vo;

import cool.scx.enumeration.RawType;
import cool.scx.http.exception.impl.NotFoundException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;

import java.io.File;
import java.io.InputStream;

/**
 * 原始文件 但不需要下载的 vo
 * 比如 pdf 之类
 *
 * @author scx567888
 * @version 0.7.0
 */
public final class Raw implements BaseVo {

    /**
     * 文件 writer
     */
    private final VoFileWriter voFileWriter;

    /**
     * bytes writer
     */
    private final VoBytesWriter voBytesWriter;

    /**
     * 数据来源是否是文件
     */
    private final boolean isFromFile;

    /**
     * 文件类型 主要用于 数据类型是 byte 时
     */
    private final RawType rawType;


    /**
     * 下载文件 不进行节流限速 文件名称为文件原始名称
     *
     * @param file a {@link java.io.File} object.
     */
    public Raw(File file) {
        this(file, null, null, true);
    }

    /**
     * <p>Constructor for Raw.</p>
     *
     * @param bytes   an array of {@link byte} objects.
     * @param rawType 文件类型 用于设置 content-type
     */
    public Raw(byte[] bytes, RawType rawType) {
        this(null, bytes, rawType, false);
    }


    /**
     * <p>Constructor for Raw.</p>
     *
     * @param file       a {@link java.io.File} object
     * @param rawType    a {@link cool.scx.enumeration.RawType} object
     * @param isFromFile a boolean
     * @param bytes      an array of {@link byte} objects
     */
    private Raw(File file, byte[] bytes, RawType rawType, boolean isFromFile) {
        this.voBytesWriter = bytes != null ? new VoBytesWriter(bytes) : null;
        this.voFileWriter = file != null ? new VoFileWriter(file) : null;
        this.rawType = rawType;
        this.isFromFile = isFromFile;
    }

    //todo 采用 静态方法获取 Raw 对象 同时 增加 对输入流的支持
    // download 同理
    public static Raw of(InputStream stream, RawType rawType) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext context) {
        if (isFromFile) {
            sendFile(context);
        } else {
            sendBytes(context);
        }
    }

    /**
     * <p>sendFile.</p>
     *
     * @param context c
     * @throws cool.scx.http.exception.impl.NotFoundException if any.
     */
    private void sendFile(RoutingContext context) throws NotFoundException {
        voFileWriter.writeFile(context);
    }

    /**
     * <p>sendBytes.</p>
     *
     * @param context a {@link io.vertx.ext.web.RoutingContext} object
     */
    private void sendBytes(RoutingContext context) {
        var response = VoHelper.fillContentType(MimeMapping.getMimeTypeForExtension(rawType.name().toLowerCase()), context.request().response())
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, "inline")
                .putHeader(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(voBytesWriter.bytes().length));
        voBytesWriter.writeBytes(response, 0);
    }

}
