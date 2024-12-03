package cool.scx.config;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.jackson.JsonNodeHelper;
import cool.scx.config.handler.ConvertValueHandler;
import cool.scx.config.handler.DefaultValueHandler;
import cool.scx.config.source.MultiConfigSource;

/**
 * 配置文件类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxConfig extends MultiConfigSource {

    public ScxConfig(ScxConfigSource... scxConfigSources) {
        super(scxConfigSources);
    }

    /**
     * 从配置文件中获取配置值
     * 没有找到会返回 null
     *
     * @param keyPath keyPath
     * @return a T object.
     */
    public JsonNode get(String keyPath) {
        return JsonNodeHelper.get(configMapping, keyPath);
    }

    public <T> T get(String keyPath, ScxConfigValueHandler<T> handler) {
        return handler.handle(keyPath, get(keyPath));
    }

    public <T> T getOrDefault(String keyPath, T defaultVal) {
        return get(keyPath, DefaultValueHandler.of(defaultVal));
    }

    public <T> T get(String keyPath, Class<T> type) {
        return get(keyPath, ConvertValueHandler.of(type));
    }

}
