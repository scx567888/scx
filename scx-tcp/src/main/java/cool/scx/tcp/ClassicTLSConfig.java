package cool.scx.tcp;

import javax.net.ssl.SSLSocket;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 经典 TLS Config
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ClassicTLSConfig implements ScxTLSConfig {

    private final SSLSocket sslSocket;

    public ClassicTLSConfig(SSLSocket sslSocket) {
        this.sslSocket = sslSocket;
    }

    @Override
    public void setUseClientMode(boolean mode) {
        sslSocket.setUseClientMode(mode);
    }

    @Override
    public void setHandshakeApplicationProtocolSelector(BiFunction<ScxTLSConfig, List<String>, String> selector) {
        sslSocket.setHandshakeApplicationProtocolSelector((sslEngine, list) -> selector.apply(this, list));
    }

    @Override
    public String getApplicationProtocol() {
        return sslSocket.getApplicationProtocol();
    }

}
