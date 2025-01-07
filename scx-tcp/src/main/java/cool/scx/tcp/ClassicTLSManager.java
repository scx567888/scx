package cool.scx.tcp;

import javax.net.ssl.SSLSocket;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 经典 TLS Manager
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ClassicTLSManager implements ScxTLSManager {

    private final SSLSocket sslSocket;

    public ClassicTLSManager(SSLSocket sslSocket) {
        this.sslSocket = sslSocket;
    }

    @Override
    public void setUseClientMode(boolean mode) {
        sslSocket.setUseClientMode(mode);
    }

    @Override
    public void setHandshakeApplicationProtocolSelector(BiFunction<ScxTLSManager, List<String>, String> selector) {
        sslSocket.setHandshakeApplicationProtocolSelector((sslEngine, list) -> selector.apply(this, list));
    }

    @Override
    public String getApplicationProtocol() {
        return sslSocket.getApplicationProtocol();
    }

}
