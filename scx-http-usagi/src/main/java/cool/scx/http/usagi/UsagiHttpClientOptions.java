package cool.scx.http.usagi;

import cool.scx.http.usagi.http1x.Http1xClientConnectionOptions;
import cool.scx.http.usagi.web_socket.WebSocketOptions;
import cool.scx.tcp.ScxTCPClientOptions;
import cool.scx.tcp.tls.TLS;

/**
 * UsagiHttpClientOptions
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpClientOptions {

    private final ScxTCPClientOptions tcpClientOptions;// TCP 客户端 配置
    private final Http1xClientConnectionOptions http1xConnectionOptions;// Http1 配置
    private final WebSocketOptions webSocketOptions;// WebSocket 配置
    private TCPClientType tcpClientType;// TCP 客户端类型
    private boolean enableHttp2; // 是否开启 Http2

    public UsagiHttpClientOptions() {
        this.tcpClientOptions = new ScxTCPClientOptions().autoUpgradeToTLS(true).autoHandshake(false);
        this.http1xConnectionOptions=new Http1xClientConnectionOptions();
        this.webSocketOptions = new WebSocketOptions();
        this.tcpClientType = TCPClientType.CLASSIC;
        this.enableHttp2 = false;//默认不启用 http2 
    }

    public UsagiHttpClientOptions(UsagiHttpClientOptions oldOptions) {
        this.tcpClientOptions = new ScxTCPClientOptions(oldOptions.tcpClientOptions()).autoUpgradeToTLS(true).autoHandshake(false);
        this.http1xConnectionOptions = new Http1xClientConnectionOptions(oldOptions.http1xConnectionOptions());
        this.webSocketOptions = new WebSocketOptions(oldOptions.webSocketOptions());
        tcpClientType(oldOptions.tcpClientType());
        enableHttp2(oldOptions.enableHttp2());
    }

    public Http1xClientConnectionOptions http1xConnectionOptions() {
        return http1xConnectionOptions;
    }

    ScxTCPClientOptions tcpClientOptions() {
        return tcpClientOptions;
    }

    public WebSocketOptions webSocketOptions() {
        return webSocketOptions;
    }

    public TCPClientType tcpClientType() {
        return tcpClientType;
    }

    public UsagiHttpClientOptions tcpClientType(TCPClientType tcpClientType) {
        this.tcpClientType = tcpClientType;
        return this;
    }


    public boolean enableHttp2() {
        return enableHttp2;
    }

    public UsagiHttpClientOptions enableHttp2(boolean enableHttp2) {
        this.enableHttp2 = enableHttp2;
        return this;
    }

    public boolean mergeWebSocketFrame() {
        return webSocketOptions.mergeWebSocketFrame();
    }

    public UsagiHttpClientOptions mergeWebSocketFrame(boolean mergeWebSocketFrame) {
        webSocketOptions.mergeWebSocketFrame(mergeWebSocketFrame);
        return this;
    }

    public int maxWebSocketFrameSize() {
        return webSocketOptions.maxWebSocketFrameSize();
    }

    public UsagiHttpClientOptions maxWebSocketFrameSize(int maxWebSocketFrameSize) {
        webSocketOptions.maxWebSocketFrameSize(maxWebSocketFrameSize);
        return this;
    }

    public int maxWebSocketMessageSize() {
        return webSocketOptions.maxWebSocketMessageSize();
    }

    public UsagiHttpClientOptions maxWebSocketMessageSize(int maxWebSocketMessageSize) {
        webSocketOptions.maxWebSocketMessageSize(maxWebSocketMessageSize);
        return this;
    }

    public TLS tls() {
        return tcpClientOptions.tls();
    }

    public UsagiHttpClientOptions tls(TLS tls) {
        this.tcpClientOptions.tls(tls);
        return this;
    }

    public enum TCPClientType {
        CLASSIC,
        NIO
    }

}
