package cool.scx.http.usagi.http1x;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.usagi.UsagiServerWebSocket;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.io.LinkedDataReader;
import cool.scx.tcp.ScxTCPSocket;

import java.io.OutputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xServerWebSocketHandshakeRequest extends Http1xServerRequest implements ScxServerWebSocketHandshakeRequest {

    public UsagiServerWebSocket webSocket;

    public Http1xServerWebSocketHandshakeRequest(Http1xRequestLine requestLine, ScxHttpHeadersWritable headers, ScxHttpBody body, ScxTCPSocket tcpSocket, LinkedDataReader dataReader, OutputStream dataWriter, boolean isKeepAlive) {
        super(requestLine, headers, body, tcpSocket, dataReader, dataWriter, isKeepAlive);
    }

    @Override
    public UsagiServerWebSocket webSocket() {
        if (webSocket == null) {
            acceptHandshake();
            webSocket = new UsagiServerWebSocket(this, dataReader, dataWriter);
        }
        return webSocket;
    }

}
