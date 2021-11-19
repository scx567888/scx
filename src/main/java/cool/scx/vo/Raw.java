package cool.scx.vo;

import cool.scx.enumeration.RawType;
import cool.scx.exception.NotFoundException;
import cool.scx.util.VoBytesWriter;
import cool.scx.util.VoFileWriter;
import cool.scx.util.VoHelper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;

import java.io.File;

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
     * @param rawType    a {@link RawType} object
     * @param isFromFile a boolean
     */
    private Raw(File file, byte[] bytes, RawType rawType, boolean isFromFile) {
        this.voBytesWriter = bytes != null ? new VoBytesWriter(bytes) : null;
        this.voFileWriter = file != null ? new VoFileWriter(file) : null;
        this.rawType = rawType;
        this.isFromFile = isFromFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(RoutingContext context) throws Exception {
        if (isFromFile) {
            sendFile(context);
        } else {
            sendBytes(context);
        }
    }

    /**
     * @param context c
     */
    private void sendFile(RoutingContext context) throws NotFoundException {
        voFileWriter.writeFile(context);
    }

    private void sendBytes(RoutingContext context) {
        var response = VoHelper.fillContentType(MimeMapping.getMimeTypeForExtension(rawType.name().toLowerCase()), context.request().response())
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, "inline")
                .putHeader(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(voBytesWriter.bytes().length));
        voBytesWriter.writeBytes(response, 0);
    }

}
