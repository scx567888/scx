package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.x.http1.status_line.Http1StatusLine;

import java.io.InputStream;

/// HTTP/1.1 ClientResponse
///
/// @author scx567888
/// @version 0.0.1
public class Http1ClientResponse implements ScxHttpClientResponse {

    private final ScxHttpStatus status;
    private final ScxHttpHeaders headers;
    private final ScxHttpBody body;
    private final String reasonPhrase;

    public Http1ClientResponse(Http1StatusLine statusLine, ScxHttpHeaders headers, InputStream bodyInputStream) {
        this.status = ScxHttpStatus.of(statusLine.code());
        this.reasonPhrase = statusLine.reason();
        this.headers = headers;
        this.body = new ScxHttpBodyImpl(bodyInputStream, this.headers);
    }

    @Override
    public ScxHttpStatus status() {
        return status;
    }

    public String reasonPhrase() {
        return reasonPhrase;
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
