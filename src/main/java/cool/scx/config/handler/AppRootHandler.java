package cool.scx.config.handler;

import cool.scx.core.ScxEnvironment;
import cool.scx.functional.ScxHandlerAR;
import cool.scx.tuple.KeyValue;
import cool.scx.util.ansi.Ansi;

import java.nio.file.Path;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class AppRootHandler implements ScxHandlerAR<KeyValue<String, Object>, Path> {

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
    public Path handle(KeyValue<String, Object> param) {
        var path = new ConvertValueHandler<>(String.class).handle(param);
        if (path != null) {
            return scxEnvironment.getPathByAppRoot(path);
        } else {
            var defaultValFile = this.defaultVal != null ? scxEnvironment.getPathByAppRoot(this.defaultVal) : null;
            Ansi.out().red("N 未检测到 " + param.key() + " , 已采用默认值 : " + defaultValFile).println();
            return defaultValFile;
        }
    }

}
