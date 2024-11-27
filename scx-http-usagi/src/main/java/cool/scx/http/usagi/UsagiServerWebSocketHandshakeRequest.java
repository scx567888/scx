package cool.scx.http.usagi;

import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.io.DataReader;
import cool.scx.net.ScxTCPSocket;

import java.io.OutputStream;

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
