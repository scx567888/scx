package cool.scx.mvc.vo;

import cool.scx.mvc.exception.ScxHttpException;
import cool.scx.mvc.exception.BadRequestException;
import cool.scx.mvc.exception.NotFoundException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>Image class.</p>
 *
 * @author scx567888
 * @version 1.0.10
 */
public abstract class Image implements BaseVo {

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
     * a
     *
     * @param file a
     * @return a
     */
    public static Image of(File file) {
        return of(file, null, null, null);
    }

    /**
     * a
     *
     * @param file   a
     * @param width  a
     * @param height a
     * @param type   a
     * @return a
     */
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
     * {@inheritDoc}
     * <p>
     * sendToClient
     */
    @Override
    public final void accept(RoutingContext context) throws BadRequestException {
        //设置缓存 减少服务器压力
        context.response()
                .putHeader(HttpHeaderNames.CACHE_CONTROL, "public,immutable,max-age=2628000")
                .putHeader(HttpHeaderNames.ACCEPT_RANGES, "bytes");
        imageHandler(context);
    }

    /**
     * a
     *
     * @param context a
     */
    public abstract void imageHandler(RoutingContext context);

    /**
     * a
     */
    private static final class SystemIconImage extends Image {

        private final Buffer buffer;

        /**
         * a
         *
         * @param file a
         */
        public SystemIconImage(File file) {
            super(file);
            this.buffer = getBuffer(file);
        }

        /**
         * 就不是普通的图片 我们就返回他在操作系统中的展示图标即可
         *
         * @return a {@link io.vertx.core.buffer.Buffer} object
         * @throws ScxHttpException if any.
         */
        private Buffer getBuffer(File file) {
            try (var out = new ByteArrayOutputStream()) {
                var image = ((ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file)).getImage();
                var myImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                var g = myImage.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                ImageIO.write(myImage, "png", out);
                return Buffer.buffer(out.toByteArray());
            } catch (Exception e) {
                throw new BadRequestException(e);
            }
        }

        @Override
        public void imageHandler(RoutingContext context) {
            context.response().putHeader(HttpHeaderNames.CONTENT_TYPE, "image/png").end(buffer);
        }

    }


    /**
     * a
     */
    private static final class OriginalImage extends Image {

        private final String filePath;

        /**
         * a
         *
         * @param file a
         */
        public OriginalImage(File file) {
            super(file);
            this.filePath = file.getPath();
        }

        @Override
        public void imageHandler(RoutingContext context) {
            context.response().sendFile(filePath);
        }

    }


    /**
     * a
     */
    private static final class CroppedImage extends Image {

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
         * @throws ScxHttpException if any.
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

}
