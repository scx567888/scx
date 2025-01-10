package cool.scx.http.usagi;

import cool.scx.http.usagi.web_socket.UsagiWebSocketOptions;
import cool.scx.tcp.ScxTCPClientOptions;

/**
 * UsagiHttpClientOptions
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpClientOptions {

    private final ScxTCPClientOptions tcpClientOptions;// TCP 客户端 配置
    private final UsagiWebSocketOptions webSocketOptions;// WebSocket 配置
    private TCPClientType tcpClientType;// TCP 客户端类型

    public UsagiHttpClientOptions() {
        this.tcpClientOptions = new ScxTCPClientOptions();
        this.webSocketOptions = new UsagiWebSocketOptions();
        this.tcpClientType = TCPClientType.CLASSIC;
    }

    public UsagiHttpClientOptions(UsagiHttpClientOptions oldOptions) {
        this.tcpClientOptions = new ScxTCPClientOptions(oldOptions.tcpClientOptions());
        this.webSocketOptions = new UsagiWebSocketOptions(oldOptions.webSocketOptions());
        tcpClientType(oldOptions.tcpClientType());
    }

    ScxTCPClientOptions tcpClientOptions() {
        return tcpClientOptions;
    }

    public UsagiWebSocketOptions webSocketOptions() {
        return webSocketOptions;
    }

    public TCPClientType tcpClientType() {
        return tcpClientType;
    }

    public UsagiHttpClientOptions tcpClientType(TCPClientType tcpClientType) {
        this.tcpClientType = tcpClientType;
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

    public enum TCPClientType {
        CLASSIC,
        NIO
    }

}
