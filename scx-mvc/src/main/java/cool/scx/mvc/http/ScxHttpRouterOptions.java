package cool.scx.mvc.http;

import java.nio.file.Path;

public class ScxHttpRouterOptions {

    public String allowedOrigin() {
        return "";
    }

    public Path uploadsDirectory() {
        return null;
    }

    public long bodyLimit() {
        return 1;
    }

}
