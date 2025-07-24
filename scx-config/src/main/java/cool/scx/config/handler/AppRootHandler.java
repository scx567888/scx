package cool.scx.config.handler;

import cool.scx.ansi.Ansi;
import cool.scx.config.ScxConfigValueHandler;
import cool.scx.config.ScxEnvironment;
import cool.scx.object.ScxObject;
import cool.scx.object.node.Node;

import java.nio.file.Path;

/// AppRootHandler
///
/// @author scx567888
/// @version 0.0.1
public final class AppRootHandler implements ScxConfigValueHandler<Path> {

    private final Path defaultPath;

    private final ScxEnvironment scxEnvironment;

    private AppRootHandler(ScxEnvironment scxEnvironment, Path defaultPath) {
        this.scxEnvironment = scxEnvironment;
        this.defaultPath = defaultPath;
    }

    public static AppRootHandler of(ScxEnvironment scxEnvironment) {
        return new AppRootHandler(scxEnvironment, null);
    }

    public static AppRootHandler of(ScxEnvironment scxEnvironment, String defaultVal) {
        return new AppRootHandler(scxEnvironment, scxEnvironment.getPathByAppRoot(defaultVal));
    }

    public static AppRootHandler of(ScxEnvironment scxEnvironment, Path defaultPath) {
        return new AppRootHandler(scxEnvironment, defaultPath);
    }

    @Override
    public Path handle(String keyPath, Node rawValue) {
        var path = ScxObject.convertValue(rawValue, String.class);
        if (path != null) {
            return this.scxEnvironment.getPathByAppRoot(path);
        } else {
            Ansi.ansi().red("N 未检测到 " + keyPath + " , 已采用默认值 : " + this.defaultPath).println();
            return this.defaultPath;
        }
    }

}
