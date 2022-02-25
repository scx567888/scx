package cool.scx.vo;

import cool.scx.http.exception.impl.NotFoundException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;

import java.io.File;

/**
 * 文件下载 vo
 *
 * @author scx567888
 * @version 1.0.10
 */
public final class Download implements BaseVo {

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
     * 下载时的文件名称
     */
    private final String downloadName;


    /**
     * 下载文件 不进行节流限速 文件名称为文件原始名称
     *
     * @param file a {@link java.io.File} object.
     */
    public Download(File file) {
        this(file, null, file.getName(), true);
    }

    /**
     * 下载文件
     *
     * @param file         待下载的文件
     * @param downloadName 下载时的名称
     */
    public Download(File file, String downloadName) {
        this(file, null, downloadName, true);
    }

    /**
     * 下载 字节数组
     *
     * @param bytes        字节数组
     * @param downloadName 下载名称 如果是 包含正确文件名的名称 则会 相应的对响应头进行处理
     */
    public Download(byte[] bytes, String downloadName) {
        this(null, bytes, downloadName, false);
    }

    /**
     * 初始化
     *
     * @param bytes        文件
     * @param downloadName 下载的文件名称
     */
    private Download(File file, byte[] bytes, String downloadName, boolean isFromFile) {
        this.downloadName = downloadName;
        this.voBytesWriter = bytes != null ? new VoBytesWriter(bytes) : null;
        this.voFileWriter = file != null ? new VoFileWriter(file) : null;
        this.isFromFile = isFromFile;
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
     * @param context c
     */
    private void sendFile(RoutingContext context) throws NotFoundException {
        context.response().putHeader(HttpHeaderNames.CONTENT_DISPOSITION, VoHelper.getDownloadContentDisposition(downloadName));
        voFileWriter.writeFile(context);
    }

    private void sendBytes(RoutingContext context) {
        var response = VoHelper.fillContentType(MimeMapping.getMimeTypeForFilename(downloadName.toLowerCase()), context.request().response())
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, VoHelper.getDownloadContentDisposition(downloadName))
                .putHeader(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(voBytesWriter.bytes().length));
        voBytesWriter.writeBytes(response, 0);
    }

}
