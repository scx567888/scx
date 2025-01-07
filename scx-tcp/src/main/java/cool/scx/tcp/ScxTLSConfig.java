package cool.scx.tcp;

import java.util.List;
import java.util.function.BiFunction;

/**
 * ScxTLSConfig
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxTLSConfig {

    void setUseClientMode(boolean mode);

    void setHandshakeApplicationProtocolSelector(BiFunction<ScxTLSConfig, List<String>, String> selector);
    
    String getApplicationProtocol();

}
