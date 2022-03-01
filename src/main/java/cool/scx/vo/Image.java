package cool.scx.vo;

import cool.scx.http.exception.impl.NotFoundException;
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
public final class Image implements BaseVo {

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

    private final File file;

    /**
     * 裁剪的宽度
     */
    private final Integer width;

    /**
     * 裁剪的高度
     */
    private final Integer height;

    /**
     * 裁剪的类型 注意!!! 只有设置 width 和 height 时生效
     * 类型及简写请参照 TYPE_POSITIONS_MAP
     * TYPE_POSITIONS_MAP
     */
    private final String type;

    /**
     * 如果图片是需要裁剪的这里存储裁剪过后的字节数组 以提高多次调用的性能
     */
    private final Buffer buffer;

    /**
     * image type 图片类型 分为原始图 , 裁剪图 和 系统图标图
     */
    private final ImageType imageType;

    /**
     * a
     */
    private final String contentType;

    /**
     * <p>Constructor for Image.</p>
     *
     * @param _file a {@link java.io.File} object.
     */
    public Image(File _file) {
        this(_file, null, null, "z");
    }

    /**
     * <p>Constructor for Image.</p>
     *
     * @param _file   a {@link java.io.File} object.
     * @param _width  a {@link java.lang.Integer} object.
     * @param _height a {@link java.lang.Integer} object.
     * @param _type   a {@link java.lang.String} object
     */
    public Image(File _file, Integer _width, Integer _height, String _type) {
        Objects.requireNonNull(_file, "图片文件不能为 null");
        // 图片不存在 这里抛出不存在异常
        if (!_file.exists()) {
            throw new NotFoundException();
        }
        file = _file;
        contentType = MimeMapping.getMimeTypeForFilename(file.getName());
        width = _width;
        height = _height;
        type = _type == null ? "z" : _type;
        imageType = initImageType();
        buffer = getBuffer();
    }

    /**
     * {@inheritDoc}
     * <p>
     * sendToClient
     */
    @Override
    public void handle(RoutingContext context) throws NotFoundException {
        var response = context.response();
        //设置缓存 减少服务器压力
        response.putHeader(HttpHeaderNames.CACHE_CONTROL, "public,immutable,max-age=2628000");
        response.putHeader(HttpHeaderNames.ACCEPT_RANGES, "bytes");
        switch (this.imageType) {
            case ORIGINAL -> response.sendFile(file.getPath());
            case CROPPED -> response.putHeader(HttpHeaderNames.CONTENT_TYPE, contentType).end(buffer);
            case SYSTEM_ICON -> response.putHeader(HttpHeaderNames.CONTENT_TYPE, "image/png").end(buffer);
        }
    }

    /**
     * 就不是普通的图片 我们就返回他在操作系统中的展示图标即可
     *
     * @return a {@link io.vertx.core.buffer.Buffer} object
     * @throws cool.scx.http.exception.impl.NotFoundException if any.
     */
    private Buffer getBufferBySystemIcon() throws NotFoundException {
        try (var out = new ByteArrayOutputStream()) {
            var image = ((ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file)).getImage();
            var myImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            var g = myImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            ImageIO.write(myImage, "png", out);
            return Buffer.buffer(out.toByteArray());
        } catch (Exception e) {
            throw new NotFoundException();
        }
    }

    /**
     * 裁剪后的图片
     *
     * @return a {@link io.vertx.core.buffer.Buffer} object
     * @throws cool.scx.http.exception.impl.NotFoundException if any.
     */
    private Buffer getBufferByCroppedPicture() throws NotFoundException {
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
            throw new NotFoundException();
        }
    }

    /**
     * <p>initImageType.</p>
     *
     * @return a {@link cool.scx.vo.Image.ImageType} object
     */
    private ImageType initImageType() {
        if (contentType != null && contentType.startsWith("image")) {
            if (height == null && width == null) {
                return ImageType.ORIGINAL;
            } else {
                return ImageType.CROPPED;
            }
        } else {
            return ImageType.SYSTEM_ICON;
        }
    }

    /**
     * <p>Getter for the field <code>buffer</code>.</p>
     *
     * @return a {@link io.vertx.core.buffer.Buffer} object
     */
    private Buffer getBuffer() {
        return switch (this.imageType) {
            case CROPPED -> getBufferByCroppedPicture();
            case SYSTEM_ICON -> getBufferBySystemIcon();
            default -> null;
        };
    }

    private enum ImageType {
        ORIGINAL, CROPPED, SYSTEM_ICON
    }

}
