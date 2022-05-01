package cool.scx.vo.impl;

import cool.scx.vo.Image;
import io.vertx.ext.web.RoutingContext;

import java.io.File;

public final class OriginalImage extends Image {

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