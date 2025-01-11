package cool.scx.http.x.http2;

import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.x.XHttpClientOptions;
import cool.scx.http.x.XHttpClientRequest;
import cool.scx.tcp.ScxTCPSocket;

public class Http2xClientConnection {

    public Http2xClientConnection(ScxTCPSocket tcpSocket, XHttpClientOptions options) {

    }

    public Http2xClientConnection sendRequest(XHttpClientRequest request, MediaWriter writer) {
        return null;
    }

    public ScxHttpClientResponse waitResponse() {
        return null;
    }

}
