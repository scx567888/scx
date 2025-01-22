package cool.scx.http.x.http1x;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.ScxHttpServerResponse;

/**
 * Http1xServerRequest
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xServerRequest extends AbstractHttp1xServerRequest {

    private final Http1xServerResponse response;

    public Http1xServerRequest(Http1xServerConnection connection, Http1xRequestLine requestLine, ScxHttpHeadersWritable headers, ScxHttpBody body) {
        super(connection, requestLine, headers, body);
        this.response = new Http1xServerResponse(connection, this);
    }

    @Override
    public ScxHttpServerResponse response() {
        return this.response;
    }

}
