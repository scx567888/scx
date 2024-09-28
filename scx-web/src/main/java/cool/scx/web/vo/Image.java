package cool.scx.web.vo;

import cool.scx.http.FileFormat;
import cool.scx.http.MediaType;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.exception.NotFoundException;
import cool.scx.http.routing.RoutingContext;
import cool.scx.http.routing.handler.StaticHelper;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.MediaType.IMAGE_PNG;

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
    protected Image(Path _file) {
        Objects.requireNonNull(_file, "图片文件不能为 null");
        // 图片不存在 这里抛出不存在异常
        if (Files.notExists(_file)) {
            throw new NotFoundException();
        }
    }

    public static Image of(Path file) {
        return of(file, null, null, null);
    }

    public static Image of(Path file, Integer width, Integer height, Position position) {
        var fileFormat = FileFormat.ofFileName(file.toString());
        if (fileFormat != null && fileFormat.mediaType().type().equals("image")) {
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
                .setHeader(CACHE_CONTROL, "public,immutable,max-age=2628000")
                .setHeader(ACCEPT_RANGES, "bytes");
        imageHandler(context);
    }

    public abstract void imageHandler(RoutingContext context);

    private static final class SystemIconImage extends Image {

        private final byte[] buffer;

        public SystemIconImage(Path file) {
            super(file);
            this.buffer = getBuffer(file);
        }

        /**
         * 就不是普通的图片 我们就返回他在操作系统中的展示图标即可
         *
         * @return a
         * @throws ScxHttpException if any.
         */
        private byte[] getBuffer(Path file) {
            try (var out = new ByteArrayOutputStream()) {
                var image = ((ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file.toFile())).getImage();
                var myImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                var g = myImage.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();
                ImageIO.write(myImage, "png", out);
                return out.toByteArray();
            } catch (Exception e) {
                throw new BadRequestException(e);
            }
        }

        @Override
        public void imageHandler(RoutingContext context) {
            context.response().contentType(ContentType.of(IMAGE_PNG)).send(buffer);
        }

    }

    private static final class OriginalImage extends Image {

        private final Path filePath;

        public OriginalImage(Path file) {
            super(file);
            this.filePath = file;
        }

        @Override
        public void imageHandler(RoutingContext context) {
            StaticHelper.sendStatic(filePath,context);
        }

    }

    private static final class CroppedImage extends Image {

        private final String contentType;

        /**
         * 如果图片是需要裁剪的这里存储裁剪过后的字节数组 以提高多次调用的性能
         */
        private final byte[] buffer;

        public CroppedImage(Path file, Integer width, Integer height, Position position) {
            super(file);
            var fileFormat = FileFormat.ofFileName(file.toString());
            var mediaType = fileFormat != null ? fileFormat.mediaType() : MediaType.APPLICATION_OCTET_STREAM;
            this.contentType = mediaType.value();
            this.buffer = getBuffer(file, width, height, position);
        }

        /**
         * 裁剪后的图片
         *
         * @return a
         * @throws ScxHttpException if any.
         */
        private byte[] getBuffer(Path file, Integer width, Integer height, Position position) {
            try (var out = new ByteArrayOutputStream()) {
                var image = Thumbnails.of(file.toFile()).scale(1.0).asBufferedImage();
                var imageHeight = image.getHeight();
                var imageWidth = image.getWidth();

                var croppedHeight = (height == null || height > imageHeight || height <= 0) ? imageHeight : height;
                var croppedWidth = (width == null || width > imageHeight || width <= 0) ? imageWidth : width;

                var absoluteSize = new AbsoluteSize(croppedWidth, croppedHeight);
                if (position != null) {
                    Thumbnails.of(file.toFile()).sourceRegion(position, absoluteSize).size(croppedWidth, croppedHeight).keepAspectRatio(false).toOutputStream(out);
                } else {
                    Thumbnails.of(file.toFile()).size(croppedWidth, croppedHeight).keepAspectRatio(false).toOutputStream(out);
                }

                return out.toByteArray();
            } catch (Exception e) {
                throw new BadRequestException(e);
            }
        }

        @Override
        public void imageHandler(RoutingContext context) {
            context.response().contentType(ContentType.of(contentType)).send(buffer);
        }

    }

}
