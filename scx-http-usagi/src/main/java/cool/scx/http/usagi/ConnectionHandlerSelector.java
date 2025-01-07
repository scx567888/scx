package cool.scx.http.usagi;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

//连接处理器选择器 用于处理 http/1.1 或 http2 
public class ConnectionHandlerSelector {

    private final Map<String, ConnectionHandler> connectionHandlers;
    private final ConnectionHandler defaultConnectionHandler;

    public ConnectionHandlerSelector() {
        this.connectionHandlers = new HashMap<>();
        var load = ServiceLoader.load(ConnectionHandler.class);
        for (var handler : load) {
            this.connectionHandlers.put(handler.supportedApplicationProtocol(), handler);
        }
        //默认采用 http1.1 处理器
        this.defaultConnectionHandler = this.connectionHandlers.get("http/1.1");
    }

    public Set<String> supportedApplicationProtocol() {
        return connectionHandlers.keySet();
    }

    public boolean checkSupport(String applicationProtocol) {
        return connectionHandlers.containsKey(applicationProtocol);
    }

    public ConnectionHandler find(String applicationProtocol) {
        return connectionHandlers.get(applicationProtocol);
    }

    public ConnectionHandler defaultConnectionHandler() {
        return defaultConnectionHandler;
    }

}
