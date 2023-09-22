package cool.scx.config;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 配置源
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxConfigSource {

    ObjectNode configMapping();

}
