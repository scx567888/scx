package cool.scx.vo.impl;

import cool.scx.vo.Image;
import io.vertx.ext.web.RoutingContext;

import java.io.File;

/**
 * a
 */
public final class OriginalImage extends Image {

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