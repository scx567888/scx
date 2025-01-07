package cool.scx.tcp;

import java.util.List;
import java.util.function.BiFunction;

public interface ScxTLSConfig {

    void setUseClientMode(boolean mode);

    void setHandshakeApplicationProtocolSelector(BiFunction<ScxTLSConfig, List<String>, String> selector);
    
    String getApplicationProtocol();

}
