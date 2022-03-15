package cool.scx.config.handler.impl;

import cool.scx.ScxEnvironment;
import cool.scx.ScxHandlerR;
import cool.scx.config.handler.ScxConfigHandlerParam;
import cool.scx.util.ansi.Ansi;

import java.io.File;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class AppRootHandler implements ScxHandlerR<ScxConfigHandlerParam, File> {

    /**
     * a
     */
    private final String defaultVal;

    /**
     * a
     */
    private final ScxEnvironment scxEnvironment;

    /**
     * <p>Constructor for AppRootHandler.</p>
     *
     * @param scxEnvironment a
     */
    public AppRootHandler(ScxEnvironment scxEnvironment) {
        this(null, scxEnvironment);
    }

    /**
     * a
     *
     * @param defaultVal     a
     * @param scxEnvironment a
     */
    public AppRootHandler(String defaultVal, ScxEnvironment scxEnvironment) {
        this.defaultVal = defaultVal;
        this.scxEnvironment = scxEnvironment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File handle(ScxConfigHandlerParam param) {
        var path = new ConvertValueHandler<>(String.class).handle(param);
        if (path != null) {
            return scxEnvironment.getFileByAppRoot(path);
        } else {
            var defaultValFile = this.defaultVal != null ? scxEnvironment.getFileByAppRoot(this.defaultVal) : null;
            Ansi.out().red("N 未检测到 " + param.ketPath() + " , 已采用默认值 : " + defaultValFile).println();
            return defaultValFile;
        }
    }

}
