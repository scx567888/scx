package cool.scx.config;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.common.util.JsonNodeHelper;
import cool.scx.config.handler.ConvertValueHandler;
import cool.scx.config.handler.DefaultValueHandler;

/**
 * 配置文件类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ScxConfig implements ScxConfigSource {

    /**
     * 当前 默认配置文件的实例
     * 是一个映射表
     * 注意!!! 如果未执行 init 或 loadConfig 方法 nowScxExample 可能为空
     */
    private final ObjectNode configMapping = JsonNodeFactory.instance.objectNode();

    /**
     * a
     *
     * @param scxConfigSources a
     */
    public ScxConfig(ScxConfigSource... scxConfigSources) {
        for (var scxConfigSource : scxConfigSources) {
            JsonNodeHelper.merge(this.configMapping, scxConfigSource.configMapping());
        }
    }

    /**
     * 从配置文件中获取配置值
     * 没有找到会返回 null
     *
     * @param keyPath keyPath
     * @return a T object.
     */
    public Object get(String keyPath) {
        return JsonNodeHelper.get(configMapping, keyPath);
    }

    /**
     * a
     *
     * @param keyPath a
     * @param handler a
     * @param <T>     a
     * @return a
     */
    public <T> T get(String keyPath, ScxConfigValueHandler<T> handler) {
        return handler.handle(keyPath, get(keyPath));
    }

    /**
     * 从默认配置文件获取配置值 并自动判断类型
     * 没有找到配置文件会返回 默认值
     *
     * @param keyPath    keyPath
     * @param defaultVal 默认值
     * @param <T>        a T object.
     * @return a T object.
     */
    public <T> T getOrDefault(String keyPath, T defaultVal) {
        return get(keyPath, DefaultValueHandler.of(defaultVal));
    }

    /**
     * 获取指定类型的配置文件 , 并尝试根据 cool.scx.type 进行转换
     *
     * @param keyPath a
     * @param type    a
     * @param <T>     a
     * @return a
     */
    public <T> T get(String keyPath, Class<T> type) {
        return get(keyPath, ConvertValueHandler.of(type));
    }

    @Override
    public ObjectNode configMapping() {
        return configMapping;
    }

}
