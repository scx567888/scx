package cool.scx.http.usagi.http1x;

import cool.scx.http.ScxHttpHeaders;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpMethod.GET;

public final class Http1xHelper {

    public static boolean checkIsWebSocketHandshake(Http1xRequestLine requestLine, ScxHttpHeaders headers) {
        if (requestLine.method() == GET) {
            var connection = headers.get(CONNECTION);
            if ("upgrade".equalsIgnoreCase(connection)) {
                var upgrade = headers.get(UPGRADE);
                return "websocket".equalsIgnoreCase(upgrade);
            }
        }
        return false;
    }

    public static boolean checkIsChunkedTransfer(ScxHttpHeaders headers) {
        var transferEncoding = headers.get(TRANSFER_ENCODING);
        return "chunked".equalsIgnoreCase(transferEncoding);
    }

}
