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
public final class Download implements BaseVo {

    /**
     * raw
     */
    private final Raw raw;

    /**
     * 下载的文件名
     */
    private final String downloadName;

    /**
     * a
     *
     * @param raw          a
     * @param downloadName a
     */
    private Download(Raw raw, String downloadName) {
        this.raw = raw;
        this.downloadName = downloadName;
    }

    public static Download of(InputStream inputStream, String downloadName) {
        return new Download(Raw.of(inputStream, null), downloadName);
    }

    public static Download of(byte[] bytes, String downloadName) {
        return new Download(Raw.of(bytes, null), downloadName);
    }

    public static Download of(Path path, String downloadName) {
        return new Download(Raw.of(path), downloadName);
    }

    public static Download of(Path path) {
        return new Download(Raw.of(path), path.getFileName().toString());
    }

    /**
     * <p>sendFile.</p>
     *
     * @param context c
     * @throws cool.scx.http.exception.impl.NotFoundException if any.
     */
    private void sendFile(RoutingContext context) throws NotFoundException {
        context.response()
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, VoHelper.getDownloadContentDisposition(downloadName));
        raw.writeFile(context);
    }

    /**
     * <p>sendBytes.</p>
     *
     * @param context a {@link io.vertx.ext.web.RoutingContext} object
     */
    private void sendBytes(RoutingContext context) {
        var response = VoHelper.fillContentType(MimeMapping.getMimeTypeForFilename(downloadName.toLowerCase()), context.request().response())
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, VoHelper.getDownloadContentDisposition(downloadName))
                .putHeader(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(raw.bytes.length));
        raw.writeBytes(response, 0);
    }

    /**
     * <p>sendBytes.</p>
     *
     * @param context a {@link io.vertx.ext.web.RoutingContext} object
     */
    private void sendInputStream(RoutingContext context) {
        var response = context.response()
                .putHeader(HttpHeaderNames.CONTENT_DISPOSITION, VoHelper.getDownloadContentDisposition(downloadName));
        raw.writeInputStream(response);
    }

    @Override
    public void handle(RoutingContext context) throws Exception {
        switch (raw.type) {
            case BYTE_ARRAY -> sendBytes(context);
            case PATH -> sendFile(context);
            case INPUT_STREAM -> sendInputStream(context);
        }
    }

}
