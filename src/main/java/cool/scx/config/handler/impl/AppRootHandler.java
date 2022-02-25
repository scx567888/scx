package cool.scx.config.handler.impl;

import cool.scx.ScxAppRoot;
import cool.scx.config.handler.ScxConfigHandlerParam;
import cool.scx.functional.ScxHandlerR;
import cool.scx.util.ansi.Ansi;

import java.io.File;

/**
 * a
 */
public final class AppRootHandler implements ScxHandlerR<ScxConfigHandlerParam, File> {

    private final String defaultVal;
    private final ScxAppRoot scxAppRoot;

    /**
     * @param scxAppRoot a
     */
    public AppRootHandler(ScxAppRoot scxAppRoot) {
        this(null, scxAppRoot);
    }

    /**
     * a
     *
     * @param defaultVal a
     * @param scxAppRoot a
     */
    public AppRootHandler(String defaultVal, ScxAppRoot scxAppRoot) {
        this.defaultVal = defaultVal;
        this.scxAppRoot = scxAppRoot;
    }

    @Override
    public File handle(ScxConfigHandlerParam param) {
        var path = new ConvertValueHandler<>(String.class).handle(param);
        if (path != null) {
            return scxAppRoot.getFileByAppRoot(path);
        } else {
            var defaultValFile = this.defaultVal != null ? scxAppRoot.getFileByAppRoot(this.defaultVal) : null;
            Ansi.out().red("N 未检测到 " + param.ketPath() + " , 已采用默认值 : " + defaultValFile).println();
            return defaultValFile;
        }
    }

}
