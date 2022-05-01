package cool.scx.vo.impl;

import cool.scx.http.exception.impl.BadRequestException;
import cool.scx.vo.Image;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public final class SystemIconImage extends Image {

    private final Buffer buffer;

    /**
     * <p>Constructor for Image.</p>
     */
    public SystemIconImage(File file) {
        super(file);
        this.buffer = getBuffer(file);
    }

    /**
     * 就不是普通的图片 我们就返回他在操作系统中的展示图标即可
     *
     * @return a {@link io.vertx.core.buffer.Buffer} object
     * @throws cool.scx.http.exception.ScxHttpException if any.
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