package cool.scx.config.source;

import java.util.Map;

/**
 * 配置源
 *
 * @author scx567888
 * @version 1.14.4
 */
public interface ScxConfigSource {

    /**
     * a
     *
     * @return a
     */
    Map<String, Object> getConfigMapping();

}
