package cool.scx.http_server.impl;

import cool.scx.http_server.ScxHttpRequest;
import cool.scx.http_server.ScxHttpResponse;

public class ScxHttpRequestImpl implements ScxHttpRequest {

    private final ScxHttpResponse response;

    public ScxHttpRequestImpl() {
        this.response=new ScxHttpResponseImpl();
    }

    @Override
    public ScxHttpResponse response() {
        return null;
    }

    @Override
    public byte[] body() {
        return new byte[0];
    }
    
}
