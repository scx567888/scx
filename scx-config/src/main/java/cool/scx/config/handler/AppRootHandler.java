package cool.scx.config.handler;

import cool.scx.ansi.Ansi;
import cool.scx.common.util.ObjectUtils;
import cool.scx.config.ScxConfigValueHandler;
import cool.scx.config.ScxEnvironment;

import java.nio.file.Path;

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
    public Path handle(String keyPath, Object rawValue) {
        var path = ObjectUtils.convertValue(rawValue, String.class);
        if (path != null) {
            return this.scxEnvironment.getPathByAppRoot(path);
        } else {
            Ansi.ansi().red("N 未检测到 " + keyPath + " , 已采用默认值 : " + this.defaultPath).println();
            return this.defaultPath;
        }
    }

}
