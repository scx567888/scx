package cool.scx.config.handler_impl;

import cool.scx.config.ScxConfigValueHandler;
import cool.scx.config.ScxEnvironment;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ansi.Ansi;

import java.nio.file.Path;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class AppRootHandler implements ScxConfigValueHandler<Path> {

    private final Path defaultPath;

    /**
     * a
     */
    private final ScxEnvironment scxEnvironment;

    /**
     * a
     *
     * @param scxEnvironment a
     * @param defaultPath    a
     */
    private AppRootHandler(ScxEnvironment scxEnvironment, Path defaultPath) {
        this.scxEnvironment = scxEnvironment;
        this.defaultPath = defaultPath;
    }

    /**
     * <p>of.</p>
     *
     * @param scxEnvironment a {@link cool.scx.config.ScxEnvironment} object
     * @return a {@link cool.scx.config.handler_impl.AppRootHandler} object
     */
    public static AppRootHandler of(ScxEnvironment scxEnvironment) {
        return new AppRootHandler(scxEnvironment, null);
    }

    /**
     * <p>of.</p>
     *
     * @param scxEnvironment a {@link cool.scx.config.ScxEnvironment} object
     * @param defaultVal     a {@link java.lang.String} object
     * @return a {@link cool.scx.config.handler_impl.AppRootHandler} object
     */
    public static AppRootHandler of(ScxEnvironment scxEnvironment, String defaultVal) {
        return new AppRootHandler(scxEnvironment, scxEnvironment.getPathByAppRoot(defaultVal));
    }

    /**
     * <p>of.</p>
     *
     * @param scxEnvironment a {@link cool.scx.config.ScxEnvironment} object
     * @param defaultPath    a {@link java.nio.file.Path} object
     * @return a {@link cool.scx.config.handler_impl.AppRootHandler} object
     */
    public static AppRootHandler of(ScxEnvironment scxEnvironment, Path defaultPath) {
        return new AppRootHandler(scxEnvironment, defaultPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path handle(String keyPath, Object rawValue) {
        var path = ObjectUtils.convertValue(rawValue, String.class);
        if (path != null) {
            return this.scxEnvironment.getPathByAppRoot(path);
        } else {
            Ansi.out().red("N 未检测到 " + keyPath + " , 已采用默认值 : " + this.defaultPath).println();
            return this.defaultPath;
        }
    }

}
