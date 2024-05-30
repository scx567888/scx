package cool.scx.web._hack;

import io.vertx.core.file.FileSystem;
import io.vertx.ext.web.RoutingContext;

public final class SendFileHelper extends StaticHandlerImpl {

    public static final SendFileHelper SEND_FILE_HELPER = new SendFileHelper();

    private SendFileHelper() {
    }

    @Override
    public String getFile(String path, RoutingContext context) {
        return path;
    }

    @Override
    public void sendStatic(RoutingContext context, FileSystem fileSystem, String path, boolean index) {
        super.sendStatic(context, fileSystem, path, false);
    }

}
