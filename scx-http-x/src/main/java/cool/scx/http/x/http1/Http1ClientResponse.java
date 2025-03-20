package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.x.http1.status_line.Http1StatusLine;

/// HTTP/1.1 ClientResponse
///
/// @author scx567888
/// @version 0.0.1
public class Http1ClientResponse implements ScxHttpClientResponse {

    private final ScxHttpStatus status;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;

    public Http1ClientResponse(Http1StatusLine statusLine, ScxHttpHeaders headers, ScxHttpBody body) {
        this.status = ScxHttpStatus.of(statusLine.code(), statusLine.reason());
        this.headers = headers;
        this.body = body;
    }

    @Override
    public ScxHttpStatus status() {
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
