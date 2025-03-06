package cool.scx.http.x.http1;

import cool.scx.http.*;

/// HTTP/1.1 ClientResponse
///
/// @author scx567888
/// @version 0.0.1
public class Http1ClientResponse implements ScxHttpClientResponse {

    private final HttpStatusCode status;
    private final ScxHttpHeadersWritable headers;
    private final ScxHttpBody body;

    public Http1ClientResponse(Http1StatusLine statusLine, ScxHttpHeadersWritable headers, ScxHttpBody body) {
        this.status = HttpStatusCode.of(statusLine.code());
        this.headers = headers;
        this.body = body;
    }

    @Override
    public HttpStatusCode status() {
        return status;
    }

    @Override
    public ScxHttpHeaders headers() {
        return headers;
    }

    @Override
    public ScxHttpBody body() {
        return body;
    }

}
