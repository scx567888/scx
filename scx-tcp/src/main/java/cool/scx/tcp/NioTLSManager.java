package cool.scx.tcp;

import javax.net.ssl.SSLEngine;
import java.util.List;
import java.util.function.BiFunction;

/**
 * NIO TLS Manager
 *
 * @author scx567888
 * @version 0.0.1
 */
public class NioTLSManager implements ScxTLSManager {

    private final SSLEngine sslEngine;

    public NioTLSManager(SSLEngine sslEngine) {
        this.sslEngine = sslEngine;
    }

    @Override
    public void setUseClientMode(boolean mode) {
        sslEngine.setUseClientMode(mode);
    }

    @Override
    public void setHandshakeApplicationProtocolSelector(BiFunction<ScxTLSManager, List<String>, String> selector) {
        sslEngine.setHandshakeApplicationProtocolSelector((sslEngine, list) -> selector.apply(this, list));
    }

    @Override
    public String getApplicationProtocol() {
        return sslEngine.getApplicationProtocol();
    }

}
