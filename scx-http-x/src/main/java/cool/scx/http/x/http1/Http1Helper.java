package cool.scx.http.x.http1;

import cool.scx.http.exception.BadRequestException;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.peer_info.PeerInfo;
import cool.scx.http.peer_info.PeerInfoWritable;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.version.HttpVersion;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.http.headers.HttpFieldName.*;
import static cool.scx.http.method.HttpMethod.GET;
import static cool.scx.http.status.HttpStatus.*;
import static cool.scx.http.x.http1.headers.transfer_encoding.EncodingType.CHUNKED;

public final class Http1Helper {

    public static final byte[] CONTINUE_100 = "HTTP/1.1 100 Continue\r\n\r\n".getBytes(StandardCharsets.UTF_8);
    public static final byte[] CRLF_BYTES = "\r\n".getBytes();
    public static final byte[] CRLF_CRLF_BYTES = "\r\n\r\n".getBytes();
    public static final byte[] CHUNKED_END_BYTES = "0\r\n\r\n".getBytes();

    public static boolean checkIsWebSocketHandshake(Http1RequestLine requestLine, ScxHttpHeaders headers) {
        if (requestLine.method() == GET) {
            var connection = headers.get(CONNECTION);
            if ("upgrade".equalsIgnoreCase(connection)) {
                var upgrade = headers.get(UPGRADE);
                return "websocket".equalsIgnoreCase(upgrade);
            }
        }
        return false;
    }

    public static boolean checkIsChunkedTransfer(Http1Headers headers) {
        var transferEncoding = headers.transferEncoding();
        if (transferEncoding == null) {
            return false;
        }
        //todo 我们暂时不支持 多个编码合并的格式 比如 GZIP, CHUNKED 这种,  后期会支持
        if (transferEncoding.size() != 1) {
            return false;
        }
        var c = transferEncoding.get(0);
        return c == CHUNKED;
    }

    public static boolean checkIsKeepAlive(Http1RequestLine requestLine, ScxHttpHeaders headers) {
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

    public static boolean checkIs100ContinueExpected(ScxHttpHeaders headers) {
        var expect = headers.get(EXPECT);
        return "100-continue".equalsIgnoreCase(expect);
    }

    public static void sendContinue100(OutputStream out) throws IOException {
        out.write(CONTINUE_100);
    }

    public static void sendChunkedEnd(OutputStream out) throws IOException {
        out.write(CHUNKED_END_BYTES);
    }

    public static void consumeInputStream(InputStream inputStream) {
        try (inputStream) {
            inputStream.transferTo(OutputStream.nullOutputStream());
        } catch (IOException e) {
            // todo 这里需要 忽略异常 还是根据 不同类型忽略
        }
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

    //验证 http1.x 中的 host
    public static void validateHeaders(ScxHttpHeadersWritable headers) {
        //我们只验证是否存在 HOST 字段 (有且只能有一个)
        var all = headers.getAll(HOST);
        int size = all.size();
        if (size == 0) {
            throw new BadRequestException("HOST header is empty");
        }
        if (size > 1) {
            throw new BadRequestException("HOST header contains more than one value");
        }
        var hostValue = all.get(0);
        if (isBlank(hostValue)) {
            throw new BadRequestException("HOST header is empty");
        }
    }

    public static boolean checkResponseHasBody(ScxHttpStatus status) {
        return SWITCHING_PROTOCOLS != status && NO_CONTENT != status && NOT_MODIFIED != status;
    }

}
