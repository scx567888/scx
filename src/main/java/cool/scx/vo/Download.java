package cool.scx.vo;

import cool.scx.http.exception.impl.NotFoundException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * 文件下载 vo
 *
 * @author scx567888
 * @version 1.0.10
 */
public final class Download extends VoWriter {

    /**
     * 下载的文件名
     */
    private final String downloadName;

    private Download(InputStream inputStream, String downloadName) {
        super(inputStream);
        this.downloadName = downloadName;
    }

    private Download(Path path, String downloadName) {
        super(path);
        this.downloadName = downloadName;
    }

    private Download(byte[] bytes, String downloadName) {
        super(bytes);
        this.downloadName = downloadName;
    }

    public static Download of(InputStream inputStream, String downloadName) {
        return new Download(inputStream, downloadName);
    }

    public static Download of(byte[] bytes, String downloadName) {
        return new Download(bytes, downloadName);
    }

    public static Download of(Path path, String downloadName) {
        return new Download(path, downloadName);
    }

    public static Download of(Path path) {
        return new Download(path, path.getFileName().toString());
    }

    @Override
    public void sendFile(RoutingContext context) throws NotFoundException {
        context.response().putHeader(HttpHeaderNames.CONTENT_DISPOSITION, BaseVo.getDownloadContentDisposition(downloadName));
        super.sendFile(context);
    }

    @Override
    public void sendBytes(RoutingContext context) {
        BaseVo.fillContentType(MimeMapping.getMimeTypeForFilename(downloadName.toLowerCase()), context.request().response()).putHeader(HttpHeaderNames.CONTENT_DISPOSITION, BaseVo.getDownloadContentDisposition(downloadName));
        super.sendBytes(context);
    }

    @Override
    public void sendInputStream(RoutingContext context) {
        BaseVo.fillContentType(MimeMapping.getMimeTypeForFilename(downloadName.toLowerCase()), context.request().response()).putHeader(HttpHeaderNames.CONTENT_DISPOSITION, BaseVo.getDownloadContentDisposition(downloadName));
        super.sendInputStream(context);
    }

}
