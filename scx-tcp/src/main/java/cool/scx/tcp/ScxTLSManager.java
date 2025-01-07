package cool.scx.tcp;

import java.util.List;
import java.util.function.BiFunction;

/**
 * ScxTLSManager
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface ScxTLSManager {

    void setUseClientMode(boolean mode);

    void setHandshakeApplicationProtocolSelector(BiFunction<ScxTLSManager, List<String>, String> selector);

    String getApplicationProtocol();

}
