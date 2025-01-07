package cool.scx.http.usagi.http1x;

import cool.scx.http.HttpVersion;
import cool.scx.http.ScxHttpHeaders;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpMethod.GET;

final class Http1xHelper {

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

    public static boolean checkIsKeepAlive(Http1xRequestLine requestLine, ScxHttpHeaders headers) {
        // Http 1.1 以下的均不支持持久连接  
        if (requestLine.version() == HttpVersion.HTTP_1_1) {
            var connection = headers.get(CONNECTION);
            // Http 1.1 中 不指定或者显示指定都表示持久连接 
            if (connection == null || "keep-alive".equalsIgnoreCase(connection)) {
                return true;
            }
            //如果显示指定 则需要关闭
            if ("close".equalsIgnoreCase(connection)) {
                return false;
            }
        }
        return false;
    }

}
