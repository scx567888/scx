package cool.scx.web.vo;

import cool.scx.common.standard.MediaType;
import cool.scx.web.exception.BadRequestException;
import cool.scx.web.exception.NotFoundException;
import cool.scx.web.exception.ScxHttpException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.impl.MimeMapping;
import io.vertx.ext.web.RoutingContext;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

import static cool.scx.common.standard.HttpFieldName.*;

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

    public static Image of(File file) {
        return of(file, null, null, null);
    }

    public static Image of(File file, Integer width, Integer height, Position position) {
        var contentType = MimeMapping.getMimeTypeForFilename(file.getName());
        if (contentType != null && contentType.startsWith("image")) {
            if (height == null && width == null) {
                return new OriginalImage(file);
            } else {
                return new CroppedImage(file, width, height, position);
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
                .putHeader(CACHE_CONTROL.toString(), "public,immutable,max-age=2628000")
                .putHeader(ACCEPT_RANGES.toString(), "bytes");
        imageHandler(context);
    }

    public abstract void imageHandler(RoutingContext context);

    private static final class SystemIconImage extends Image {

        private final Buffer buffer;

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
            context.response().putHeader(CONTENT_TYPE.toString(), MediaType.IMAGE_PNG.toString()).end(buffer);
        }

    }

    private static final class OriginalImage extends Image {

        private final String filePath;

        public OriginalImage(File file) {
            super(file);
            this.filePath = file.getPath();
        }

        @Override
        public void imageHandler(RoutingContext context) {
            context.response().sendFile(filePath);
        }

    }

    private static final class CroppedImage extends Image {

        private final String contentType;

        /**
         * 如果图片是需要裁剪的这里存储裁剪过后的字节数组 以提高多次调用的性能
         */
        private final Buffer buffer;

        public CroppedImage(File file, Integer width, Integer height, Position position) {
            super(file);
            this.contentType = MimeMapping.getMimeTypeForFilename(file.getName());
            this.buffer = getBuffer(file, width, height, position);
        }

        /**
         * 裁剪后的图片
         *
         * @return a {@link io.vertx.core.buffer.Buffer} object
         * @throws ScxHttpException if any.
         */
        private Buffer getBuffer(File file, Integer width, Integer height, Position position) {
            try (var out = new ByteArrayOutputStream()) {
                var image = Thumbnails.of(file).scale(1.0).asBufferedImage();
                var imageHeight = image.getHeight();
                var imageWidth = image.getWidth();

                var croppedHeight = (height == null || height > imageHeight || height <= 0) ? imageHeight : height;
                var croppedWidth = (width == null || width > imageHeight || width <= 0) ? imageWidth : width;

                var absoluteSize = new AbsoluteSize(croppedWidth, croppedHeight);
                if (position != null) {
                    Thumbnails.of(file).sourceRegion(position, absoluteSize).size(croppedWidth, croppedHeight).keepAspectRatio(false).toOutputStream(out);
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
            context.response().putHeader(CONTENT_TYPE.toString(), contentType).end(buffer);
        }

    }

}
