package cool.scx.config.source;

import java.util.Map;

/**
 * 配置源
 */
public interface ScxConfigSource {

    /**
     * a
     *
     * @return a
     */
    Map<String, Object> getConfigMapping();

}
