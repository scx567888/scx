package cool.scx.http_server.impl.scx;

import cool.scx.http_server.ScxHttpRequest;
import cool.scx.http_server.ScxHttpResponse;
import cool.scx.http_server.ScxTCPSocket;

public class ScxHttpRequestImpl implements ScxHttpRequest {

    private final ScxHttpResponse response;
    private final ScxTCPSocket tcpSocket;

    public ScxHttpRequestImpl(ScxTCPSocket tcpSocket) {
        this.tcpSocket = tcpSocket;
        this.response = new ScxHttpResponseImpl(tcpSocket);
    }

    @Override
    public ScxHttpResponse response() {
        return response;
    }

}
