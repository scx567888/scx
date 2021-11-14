package cool.scx.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.ScxHandlerR;
import cool.scx.config.handler.ScxConfigHandlerParam;
import cool.scx.config.handler.impl.ConvertValueHandler;
import cool.scx.config.handler.impl.DefaultValueHandler;
import cool.scx.util.MapUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ansi.Ansi;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置文件类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ScxConfig {

    /**
     * 当前 默认配置文件的实例
     * 是一个映射表
     * 注意!!! 如果未执行 init 或 loadConfig 方法 nowScxExample 可能为空
     */
    private final Map<String, Object> configMapping = new LinkedHashMap<>();

    /**
     * a
     *
     * @param scxConfigFile a
     * @param args          a
     */
    public ScxConfig(File scxConfigFile, String[] args) {
        try {
            if (scxConfigFile == null || !scxConfigFile.exists()) {
                throw new ScxConfigFileMissingException();
            }
            configMapping.putAll(MapUtils.flatMap(ObjectUtils.readValueToMap(scxConfigFile), null));
            Ansi.out().brightBlue("Y 已加载配置文件 : " + scxConfigFile.getPath()).println();
        } catch (Exception e) {
            if (e instanceof ScxConfigFileMissingException) {
                Ansi.out().red("N 配置文件已丢失!!! 请确保配置文件存在 scx-config.json").println();
            } else if (e instanceof JsonProcessingException) {
                Ansi.out().red("N 配置文件已损坏!!! 请确保配置文件正确 scx-config.json").println();
            } else {
                e.printStackTrace();
            }
        }
        for (var arg : args) {
            if (arg.startsWith("--")) {
                var strings = arg.substring(2).split("=");
                if (strings.length == 2) {
                    configMapping.put(strings[0], strings[1]);
                }
            }
        }
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
        return get(keyPath, new DefaultValueHandler<>(defaultVal));
    }

    /**
     * 从配置文件中获取配置值
     * 没有找到会返回 null
     *
     * @param keyPath keyPath
     * @return a T object.
     */
    public Object get(String keyPath) {
        return configMapping.get(keyPath);
    }

    /**
     * a
     *
     * @param keyPath  a
     * @param handlerR a
     * @param <T>      a
     * @return a
     */
    public <T> T get(String keyPath, ScxHandlerR<ScxConfigHandlerParam, T> handlerR) {
        return handlerR.handle(new ScxConfigHandlerParam(keyPath, get(keyPath)));
    }

    /**
     * 获取指定类型的配置文件 , 并尝试根据 type 进行转换
     *
     * @param keyPath a
     * @param type    a
     * @param <T>     a
     * @return a
     */
    public <T> T get(String keyPath, Class<T> type) {
        return get(keyPath, new ConvertValueHandler<>(type));
    }

    /**
     * 获得 configMapping
     *
     * @return config
     */
    public Map<String, Object> configMapping() {
        return new HashMap<>(configMapping);
    }

    /**
     * 配置文件丢失异常
     */
    private static class ScxConfigFileMissingException extends Exception {

    }

}
