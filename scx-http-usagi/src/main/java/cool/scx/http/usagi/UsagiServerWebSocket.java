package cool.scx.http.usagi;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.io.DataReader;

import java.io.OutputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiServerWebSocket extends UsagiWebSocket implements ScxServerWebSocket {

    private final UsagiServerWebSocketHandshakeRequest handshakeServerRequest;

    public UsagiServerWebSocket(UsagiServerWebSocketHandshakeRequest handshakeServerRequest, DataReader reader, OutputStream writer) {
        super(handshakeServerRequest.tcpSocket, reader, writer);
        this.handshakeServerRequest = handshakeServerRequest;
    }

    @Override
    public ScxURI uri() {
        return this.handshakeServerRequest.uri();
    }

    @Override
    public ScxHttpHeaders headers() {
        return this.handshakeServerRequest.headers();
    }

}
