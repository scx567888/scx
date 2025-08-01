package cool.scx.http.x.http1;

import cool.scx.http.exception.BadRequestException;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.peer_info.PeerInfo;
import cool.scx.http.peer_info.PeerInfoWritable;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.headers.upgrade.ScxUpgrade;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.io.io_stream.StreamClosedException;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.http.headers.HttpFieldName.HOST;
import static cool.scx.http.method.HttpMethod.GET;
import static cool.scx.http.status.HttpStatus.*;
import static cool.scx.http.x.http1.headers.connection.Connection.UPGRADE;

public final class Http1Helper {

    public static final byte[] CONTINUE_100 = "HTTP/1.1 100 Continue\r\n\r\n".getBytes(StandardCharsets.UTF_8);
    public static final byte[] CRLF_BYTES = "\r\n".getBytes();
    public static final byte[] CRLF_CRLF_BYTES = "\r\n\r\n".getBytes();
    public static final byte[] CHUNKED_END_BYTES = "0\r\n\r\n".getBytes();

    /// 检查是不是 升级请求 如果不是 返回 null
    public static ScxUpgrade checkUpgradeRequest(Http1RequestLine requestLine, Http1Headers headers) {
        return requestLine.method() == GET && headers.connection() == UPGRADE ? headers.upgrade() : null;
    }

    public static void sendContinue100(OutputStream out) throws IOException {
        out.write(CONTINUE_100);
    }

    public static void consumeInputStream(InputStream inputStream) {
        try (inputStream) {
            inputStream.transferTo(OutputStream.nullOutputStream());
        } catch (StreamClosedException | IOException e) {
            // 忽略
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

    /// 验证 Http/1.1 中的 Host, 这里我们只校验是否存在且只有一个值
    public static void validateHost(ScxHttpHeadersWritable headers) {
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
        return SWITCHING_PROTOCOLS != status &&
               NO_CONTENT != status &&
               NOT_MODIFIED != status;
    }

    public static boolean checkRequestHasBody(ScxHttpMethod method) {
        return GET != method;
    }

    /// 事实上我们无法拿到真正的地址 所以这里只是推测而已
    public static ScxURI inferURI(ScxURI requestLineTarget, ScxHttpHeaders headers, ScxTCPSocket tcpSocket) {
        var uri = ScxURI.of(requestLineTarget);
        //1, 有可能已经是全路径 我们判断一下是否存在协议
        if (uri.scheme() != null) {
            return uri;
        }
        //2, 推测协议
        if (tcpSocket.isTLS()) {
            uri.scheme("https");
        } else {
            uri.scheme("http");
        }
        //3, 开始推测 host 和端口号
        var host = headers.get(HOST);
        if (host != null) {
            var authority = ScxURI.ofAuthority(host);
            uri.host(authority.host()).port(authority.port());
        } else {
            var localAddress = tcpSocket.localAddress();
            uri.host(localAddress.getHostString()).port(localAddress.getPort());
        }
        return uri;
    }

}
