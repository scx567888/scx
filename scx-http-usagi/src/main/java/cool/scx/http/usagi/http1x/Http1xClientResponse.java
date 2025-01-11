package cool.scx.http.usagi.http1x;

import cool.scx.http.*;

/**
 * Http1xClientResponse
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xClientResponse implements ScxHttpClientResponse {

    private final HttpStatusCode status;
    private final ScxHttpHeadersWritable headers;
    private final ScxHttpBody body;

    public Http1xClientResponse(Http1xStatusLine statusLine, ScxHttpHeadersWritable headers, ScxHttpBody body) {
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
