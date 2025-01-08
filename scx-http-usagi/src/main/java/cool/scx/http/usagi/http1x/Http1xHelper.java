package cool.scx.http.usagi.http1x;

import cool.scx.http.HttpVersion;
import cool.scx.http.PeerInfo;
import cool.scx.http.PeerInfoWritable;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.tcp.ScxTCPSocket;

import static cool.scx.http.HttpFieldName.*;
import static cool.scx.http.HttpMethod.GET;

final class Http1xHelper {

    public static final byte[] CRLF_BYTES = "\r\n".getBytes();
    public static final byte[] CRLF_CRLF_BYTES = "\r\n\r\n".getBytes();

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

    public static PeerInfoWritable getRemotePeer(ScxTCPSocket tcpSocket) {
        var address = tcpSocket.remoteAddress();
        //todo 未完成 tls 信息没有写入
        return PeerInfo.of().address(address).host(address.getHostString()).port(address.getPort());
    } 
    
    public static PeerInfoWritable getLocalPeer(ScxTCPSocket tcpSocket) {
        //todo 未完成 tls 信息没有写入
        var address = tcpSocket.localAddress();
        return PeerInfo.of().address(address).host(address.getHostString()).port(address.getPort());
    }

}
