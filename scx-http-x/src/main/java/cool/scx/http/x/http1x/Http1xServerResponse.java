package cool.scx.http.x.http1x;

import cool.scx.http.ScxHttpServerResponse;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xServerResponse extends AbstractHttp1xServerResponse<Http1xServerRequest> implements ScxHttpServerResponse {

    Http1xServerResponse(Http1xServerConnection connection, Http1xServerRequest request) {
        super(connection, request);
    }

}
