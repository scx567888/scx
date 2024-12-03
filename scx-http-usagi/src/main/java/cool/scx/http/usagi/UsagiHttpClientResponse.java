package cool.scx.http.usagi;

import cool.scx.http.*;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpClientResponse implements ScxHttpClientResponse {

    private final HttpStatusCode status;
    private final ScxHttpHeadersWritable headers;
    private final ScxHttpBody body;

    public UsagiHttpClientResponse(HttpStatusCode status, ScxHttpHeadersWritable headers, ScxHttpBody body) {
        this.status = status;
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
