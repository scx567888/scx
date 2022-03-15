package cool.scx.config.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.config.ScxConfigSource;
import cool.scx.util.MapUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ansi.Ansi;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public final class JsonFileConfigSource implements ScxConfigSource {

    private final LinkedHashMap<String, Object> configMapping = new LinkedHashMap<>();

    private JsonFileConfigSource(File jsonFile) {
        if (jsonFile == null) {
            throw new IllegalArgumentException("jsonFile 不能为空 !!!");
        }
        try {
            if (!jsonFile.exists()) {
                throw new JsonConfigFileMissingException();
            }
            var rawMap = ObjectUtils.jsonMapper().readValue(jsonFile, ObjectUtils.MAP_TYPE);
            configMapping.putAll(MapUtils.flatMap(rawMap, null));
            Ansi.out().brightBlue("Y 已加载配置文件 : " + jsonFile.getPath()).println();
        } catch (Exception e) {
            if (e instanceof JsonConfigFileMissingException) {
                Ansi.out().red("N 配置文件已丢失!!! 请确保配置文件存在 " + jsonFile.getName()).println();
            } else if (e instanceof JsonProcessingException) {
                Ansi.out().red("N 配置文件已损坏!!! 请确保配置文件格式正确 " + jsonFile.getName()).println();
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * a
     *
     * @param jsonFile a
     * @return a
     */
    public static JsonFileConfigSource of(File jsonFile) {
        return new JsonFileConfigSource(jsonFile);
    }

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
