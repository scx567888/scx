package cool.scx.http.usagi;

import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.io.DataReader;
import cool.scx.tcp.ScxTCPSocket;

import java.io.OutputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiServerWebSocketHandshakeRequest extends UsagiHttpServerRequest implements ScxServerWebSocketHandshakeRequest {

    private final DataReader reader;
    private final OutputStream writer;
    public UsagiServerWebSocket webSocket;

    public UsagiServerWebSocketHandshakeRequest(ScxTCPSocket tcpSocket, DataReader reader, OutputStream writer) {
        super(tcpSocket);
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public UsagiServerWebSocket webSocket() {
        if (webSocket == null) {
            acceptHandshake();
            webSocket = new UsagiServerWebSocket(this, reader, writer);
        }
        return webSocket;
    }

}
