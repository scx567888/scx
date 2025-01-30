package cool.scx.tcp;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import java.util.List;
import java.util.function.BiFunction;

/**
 * NIO TLS Manager
 *
 * @author scx567888
 * @version 0.0.1
 */
public class AioTLSManager implements ScxTLSManager {

    private final SSLEngine sslEngine;

    public AioTLSManager(SSLEngine sslEngine) {
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

    @Override
    public SSLParameters getSSLParameters() {
        return sslEngine.getSSLParameters();
    }

    @Override
    public void setSSLParameters(SSLParameters params) {
        sslEngine.setSSLParameters(params);
    }

}
