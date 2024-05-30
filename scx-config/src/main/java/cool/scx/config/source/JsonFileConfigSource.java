package cool.scx.config.source;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.common.ansi.Ansi;
import cool.scx.common.util.ObjectUtils;
import cool.scx.config.ScxConfigSource;

import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonFileConfigSource implements ScxConfigSource {

    private final ObjectNode configMapping = JsonNodeFactory.instance.objectNode();

    private JsonFileConfigSource(Path jsonPath) {
        if (jsonPath == null) {
            throw new IllegalArgumentException("jsonPath 不能为空 !!!");
        }
        try {
            if (Files.notExists(jsonPath)) {
                throw new JsonConfigFileMissingException();
            }
            var rawMap = ObjectUtils.jsonMapper().readTree(jsonPath.toFile());
            if (rawMap instanceof ObjectNode objectNode) {
                configMapping.setAll(objectNode);
                Ansi.ansi().brightBlue("Y 已加载配置文件 : " + jsonPath).println();
            } else {
                throw new JsonConfigFileNotObjectException();
            }
        } catch (Exception e) {
            switch (e) {
                case JsonConfigFileMissingException jsonConfigFileMissingException ->
                        Ansi.ansi().red("N 配置文件已丢失!!! 请确保配置文件存在 : " + jsonPath).println();
                case JsonProcessingException jsonProcessingException ->
                        Ansi.ansi().red("N 配置文件已损坏!!! 请确保配置文件格式正确 : " + jsonPath).println();
                case JsonConfigFileNotObjectException jsonConfigFileNotObjectException ->
                        Ansi.ansi().red("N 配置文件必须为 Object 格式!!! 请确保配置文件格式正确 : " + jsonPath).println();
                default -> e.printStackTrace();
            }
        }
    }

    public static JsonFileConfigSource of(Path jsonPath) {
        return new JsonFileConfigSource(jsonPath);
    }

    @Override
    public ObjectNode configMapping() {
        return configMapping;
    }

    /**
     * 配置文件丢失异常
     */
    private static class JsonConfigFileMissingException extends Exception {

    }

    private static class JsonConfigFileNotObjectException extends Exception {

    }

}
