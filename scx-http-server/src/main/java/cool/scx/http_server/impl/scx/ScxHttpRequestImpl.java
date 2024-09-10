package cool.scx.http_server.impl.scx;

import cool.scx.http_server.*;

public class ScxHttpRequestImpl implements ScxHttpRequest {

    private final ScxHttpResponse response;
    private final ScxTCPSocket tcpSocket;

    public ScxHttpRequestImpl(ScxTCPSocket tcpSocket) {
        this.tcpSocket = tcpSocket;
        this.response = new ScxHttpResponseImpl(tcpSocket);
    }

    @Override
    public ScxHttpMethod method() {
        return null;
    }

    @Override
    public ScxHttpPath path() {
        return null;
    }

    @Override
    public ScxHttpVersion version() {
        return null;
    }

    @Override
    public ScxHttpHeaders headers() {
        return null;
    }

    @Override
    public ScxHttpBody body() {
        return null;
    }

    @Override
    public ScxHttpResponse response() {
        return response;
    }

}
