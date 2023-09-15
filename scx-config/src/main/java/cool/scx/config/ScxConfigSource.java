package cool.scx.config;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 配置源
 *
 * @author scx567888
 * @version 2.4.9
 */
public interface ScxConfigSource {

    ObjectNode configMapping();

}
