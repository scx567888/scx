package cool.scx.vo.impl;

import cool.scx.http.exception.impl.BadRequestException;
import cool.scx.vo.Image;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Positions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * a
 */
public final class CroppedImage extends Image {

    /**
     * type 和裁剪类型 映射表
     */
    private static final Map<String, Positions> TYPE_POSITIONS_MAP = new HashMap<>();

    static {
        TYPE_POSITIONS_MAP.put("top-left", Positions.TOP_LEFT);
        TYPE_POSITIONS_MAP.put("top-center", Positions.TOP_CENTER);
        TYPE_POSITIONS_MAP.put("top-right", Positions.TOP_RIGHT);
        TYPE_POSITIONS_MAP.put("center-left", Positions.CENTER_LEFT);
        TYPE_POSITIONS_MAP.put("center", Positions.CENTER);
        TYPE_POSITIONS_MAP.put("center-center", Positions.CENTER);
        TYPE_POSITIONS_MAP.put("center-right", Positions.CENTER_RIGHT);
        TYPE_POSITIONS_MAP.put("bottom-left", Positions.BOTTOM_LEFT);
        TYPE_POSITIONS_MAP.put("bottom-center", Positions.BOTTOM_CENTER);
        TYPE_POSITIONS_MAP.put("bottom-right", Positions.BOTTOM_RIGHT);
        //简写
        TYPE_POSITIONS_MAP.put("tl", Positions.TOP_LEFT);
        TYPE_POSITIONS_MAP.put("tc", Positions.TOP_CENTER);
        TYPE_POSITIONS_MAP.put("tr", Positions.TOP_RIGHT);
        TYPE_POSITIONS_MAP.put("cl", Positions.CENTER_LEFT);
        TYPE_POSITIONS_MAP.put("c", Positions.CENTER);
        TYPE_POSITIONS_MAP.put("cc", Positions.CENTER);
        TYPE_POSITIONS_MAP.put("cr", Positions.CENTER_RIGHT);
        TYPE_POSITIONS_MAP.put("bl", Positions.BOTTOM_LEFT);
        TYPE_POSITIONS_MAP.put("bc", Positions.BOTTOM_CENTER);
        TYPE_POSITIONS_MAP.put("br", Positions.BOTTOM_RIGHT);
    }

    /**
     * a
     */
    private final String contentType;

    /**
     * 如果图片是需要裁剪的这里存储裁剪过后的字节数组 以提高多次调用的性能
     */
    private final Buffer buffer;

    /**
     * a
     *
     * @param file   a
     * @param width  a
     * @param height a
     * @param type   a
     */
    public CroppedImage(File file, Integer width, Integer height, String type) {
        super(file);
        this.contentType = MimeMapping.getMimeTypeForFilename(file.getName());
        this.buffer = getBuffer(file, width, height, type == null ? "z" : type);
    }

    /**
     * 裁剪后的图片
     *
     * @return a {@link io.vertx.core.buffer.Buffer} object
     * @throws cool.scx.http.exception.ScxHttpException if any.
     */
    private Buffer getBuffer(File file, Integer width, Integer height, String type) {
        try (var out = new ByteArrayOutputStream()) {
            var image = Thumbnails.of(file).scale(1.0).asBufferedImage();
            var imageHeight = image.getHeight();
            var imageWidth = image.getWidth();

            var croppedHeight = (height == null || height > imageHeight || height <= 0) ? imageHeight : height;
            var croppedWidth = (width == null || width > imageHeight || width <= 0) ? imageWidth : width;

            var absoluteSize = new AbsoluteSize(croppedWidth, croppedHeight);
            var positions = TYPE_POSITIONS_MAP.get(type.toLowerCase());
            if (positions != null) {
                Thumbnails.of(file).sourceRegion(positions, absoluteSize).size(croppedWidth, croppedHeight).keepAspectRatio(false).toOutputStream(out);
            } else {
                Thumbnails.of(file).size(croppedWidth, croppedHeight).keepAspectRatio(false).toOutputStream(out);
            }

            return Buffer.buffer(out.toByteArray());
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
    }

    @Override
    public void imageHandler(RoutingContext context) {
        context.response().putHeader(HttpHeaderNames.CONTENT_TYPE, contentType).end(buffer);
    }

}