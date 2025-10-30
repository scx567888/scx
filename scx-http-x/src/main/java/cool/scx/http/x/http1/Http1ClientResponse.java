package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.body.ScxHttpBody;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.status_line.Http1StatusLine;
import cool.scx.io.ByteInput;

/// HTTP/1.1 ClientResponse
///
/// @author scx567888
/// @version 0.0.1
public class Http1ClientResponse implements ScxHttpClientResponse {

    private final ScxHttpStatus status;
    private final Http1Headers headers;
    private final Http1Body body;
    private final String reasonPhrase;

    public Http1ClientResponse(Http1StatusLine statusLine, Http1Headers headers, ByteInput bodyByteInput) {
        this.status = ScxHttpStatus.of(statusLine.code());
        this.reasonPhrase = statusLine.reason();
        this.headers = headers;
        this.body = new Http1Body(bodyByteInput, this.headers);
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
