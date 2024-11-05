package cool.scx.config;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.function.BiConsumer;

/**
 * 配置源
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxConfigSource {

    ObjectNode configMapping();

    default void onChange(BiConsumer<ObjectNode, ObjectNode> changeHandler) {

    }

}
