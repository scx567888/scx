package cool.scx.config;

import java.util.Map;

/**
 * 配置源
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxConfigSource {

    /**
     * a
     *
     * @return a
     */
    Map<String, Object> getConfigMapping();

}
