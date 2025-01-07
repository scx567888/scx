package cool.scx.tcp;

import javax.net.ssl.SSLEngine;
import java.util.List;
import java.util.function.BiFunction;

public class NioTLSConfig implements ScxTLSConfig {

    private final SSLEngine sslEngine;

    public NioTLSConfig(SSLEngine sslEngine) {
        this.sslEngine = sslEngine;
    }

    @Override
    public void setUseClientMode(boolean mode) {
        sslEngine.setUseClientMode(mode);
    }

    @Override
    public void setHandshakeApplicationProtocolSelector(BiFunction<ScxTLSConfig, List<String>, String> selector) {
        sslEngine.setHandshakeApplicationProtocolSelector((sslEngine, list) -> selector.apply(this, list));
    }

}
