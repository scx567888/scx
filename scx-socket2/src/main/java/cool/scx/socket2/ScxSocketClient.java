package cool.scx.socket2;

import cool.scx.http.ScxHttpClient;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static cool.scx.common.util.RandomUtils.randomUUID;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.getLogger;

public class ScxSocketClient {

    private static final System.Logger logger = getLogger(ScxSocketClient.class.getName());

    private final ScxURIWritable uri;
    private final ScxHttpClient httpClient;
    private final String clientID;
    private final ScxSocketClientOptions options;
    private final ScheduledExecutorService scheduledExecutor;
    private final Executor executor;

    private Consumer<ScxClientSocket> connectHandler;
    private ScxClientSocket clientSocket;

    public ScxSocketClient(String uri, ScxHttpClient httpClient, String clientID, ScxSocketClientOptions options) {
        this.uri = ScxURI.of(uri).addQuery("clientID", clientID);
        this.httpClient = httpClient;
        this.clientID = clientID;
        this.options = options;
        this.scheduledExecutor = options.scheduledExecutor();
        this.executor = options.executor();
    }

    public ScxSocketClient(String uri, ScxHttpClient httpClient, String clientID) {
        this(uri, httpClient, clientID, new ScxSocketClientOptions());
    }

    public ScxSocketClient(String uri, ScxHttpClient httpClient, ScxSocketClientOptions options) {
        this(uri, httpClient, randomUUID(), options);
    }

    public ScxSocketClient(String uri, ScxHttpClient httpClient) {
        this(uri, httpClient, randomUUID(), new ScxSocketClientOptions());
    }

    //不要阻塞这个回调 这个回调只用于设置 ScxSocket 的回调
    public void onConnect(Consumer<ScxClientSocket> onConnect) {
        this.connectHandler = onConnect;
    }

    public void connect() {
        try {
            httpClient.webSocketHandshakeRequest().uri(uri).onWebSocket(webSocket -> {
                this.clientSocket = new ScxClientSocket(webSocket);
                _callOnConnect(this.clientSocket);
            });
        } catch (Exception e) {
            //连接失败我们重新连接
            this.reconnect(e);
        }
    }

    private void reconnect(Exception e) {
        logger.log(DEBUG, "WebSocket 重连中... CLIENT_ID : {0}", clientID, e);
        scheduledExecutor.schedule(() -> {
            this.connect();
        }, 1000, TimeUnit.MILLISECONDS);
    }

    private void _callOnConnect(ScxClientSocket clientSocket) {
        if (this.connectHandler != null) {
            connectHandler.accept(clientSocket);
        }
    }

}
