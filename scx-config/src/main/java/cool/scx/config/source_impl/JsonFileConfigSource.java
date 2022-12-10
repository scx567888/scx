package cool.scx.config.source_impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.config.ScxConfigSource;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ansi.Ansi;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>JsonFileConfigSource class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class JsonFileConfigSource implements ScxConfigSource {

    private final LinkedHashMap<String, Object> configMapping = new LinkedHashMap<>();

    /**
     * <p>Constructor for JsonFileConfigSource.</p>
     *
     * @param jsonPath a {@link java.io.File} object
     */
    private JsonFileConfigSource(Path jsonPath) {
        if (jsonPath == null) {
            throw new IllegalArgumentException("jsonPath 不能为空 !!!");
        }
        try {
            if (Files.notExists(jsonPath)) {
                throw new JsonConfigFileMissingException();
            }
            var rawMap = ObjectUtils.jsonMapper().readValue(jsonPath.toFile(), ObjectUtils.MAP_TYPE);
            configMapping.putAll(ObjectUtils.flatMap(rawMap));
            Ansi.out().brightBlue("Y 已加载配置文件 : " + jsonPath).println();
        } catch (Exception e) {
            if (e instanceof JsonConfigFileMissingException) {
                Ansi.out().red("N 配置文件已丢失!!! 请确保配置文件存在 : " + jsonPath).println();
            } else if (e instanceof JsonProcessingException) {
                Ansi.out().red("N 配置文件已损坏!!! 请确保配置文件格式正确 : " + jsonPath).println();
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * a
     *
     * @param jsonPath a
     * @return a
     */
    public static JsonFileConfigSource of(Path jsonPath) {
        return new JsonFileConfigSource(jsonPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getConfigMapping() {
        return configMapping;
    }

    /**
     * 配置文件丢失异常
     */
    private static class JsonConfigFileMissingException extends Exception {

    }

}
