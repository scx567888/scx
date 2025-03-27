package cool.scx.http.x.http1;

import cool.scx.http.exception.BadRequestException;
import cool.scx.http.exception.ContentTooLargeException;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.peer_info.PeerInfo;
import cool.scx.http.peer_info.PeerInfoWritable;
import cool.scx.http.status.ScxHttpStatus;
import cool.scx.http.x.http1.chunked.HttpChunkedDataSupplier;
import cool.scx.http.x.http1.exception.RequestHeaderFieldsTooLargeException;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_reader.PowerfulLinkedDataReader;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;
import cool.scx.io.io_stream.DataReaderInputStream;
import cool.scx.io.io_stream.FixedLengthDataReaderInputStream;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.http.headers.HttpFieldName.HOST;
import static cool.scx.http.headers.ScxHttpHeadersHelper.parseHeaders;
import static cool.scx.http.method.HttpMethod.GET;
import static cool.scx.http.status.HttpStatus.*;
import static cool.scx.http.x.http1.headers.connection.Connection.UPGRADE;
import static cool.scx.http.x.http1.headers.transfer_encoding.TransferEncoding.CHUNKED;
import static cool.scx.http.x.http1.headers.upgrade.Upgrade.WEB_SOCKET;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class Http1Helper {

    public static final byte[] CONTINUE_100 = "HTTP/1.1 100 Continue\r\n\r\n".getBytes(StandardCharsets.UTF_8);
    public static final byte[] CRLF_BYTES = "\r\n".getBytes();
    public static final byte[] CRLF_CRLF_BYTES = "\r\n\r\n".getBytes();
    public static final byte[] CHUNKED_END_BYTES = "0\r\n\r\n".getBytes();

    public static boolean checkIsWebSocketHandshake(Http1RequestLine requestLine, Http1Headers headers) {
        return requestLine.method() == GET &&
                headers.connection() == UPGRADE &&
                headers.upgrade() == WEB_SOCKET;
    }

    public static void sendContinue100(OutputStream out) throws IOException {
        out.write(CONTINUE_100);
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

    public static InputStream readBodyInputStream(Http1Headers headers, PowerfulLinkedDataReader dataReader, long maxPayloadSize) {
        // HTTP/1.1 本质上只有两种请求体格式 1, 分块传输 2, 指定长度 (当然也可以没有长度 那就表示没有请求体)

        //1, 因为 分块传输的优先级高于 contentLength 所以先判断是否为分块传输
        var transferEncoding = headers.transferEncoding();
        if (transferEncoding == CHUNKED) {
            return new DataReaderInputStream(new HttpChunkedDataSupplier(dataReader, maxPayloadSize));
        }

        //2, 判断请求体是不是有 指定长度
        var contentLength = headers.contentLength();
        if (contentLength != null) {
            // 请求体长度过大 这里抛出异常
            if (contentLength > maxPayloadSize) {
                throw new ContentTooLargeException();
            }
            return new FixedLengthDataReaderInputStream(dataReader, contentLength);
        }

        //3, 没有长度的空请求体
        return InputStream.nullInputStream();
    }

    public static Http1Headers readHeaders(DataReader dataReader, int maxHeaderSize) {
        //尝试读取 headers
        try {
            // 1, 尝试检查空头的情况 , 即请求行后紧跟 \r\n
            var b = dataReader.peek(2);
            if (Arrays.equals(b, CRLF_BYTES)) {
                dataReader.skip(2);
                return new Http1Headers();
            }

            // 2, 尝试正常读取 , 读取到 第一个 \r\n\r\n 为止
            var headerBytes = dataReader.readUntil(CRLF_CRLF_BYTES, maxHeaderSize);
            var headerStr = new String(headerBytes, UTF_8);
            return parseHeaders(new Http1Headers(), headerStr);
        } catch (NoMoreDataException | UncheckedIOException e) {
            // Socket 关闭了 或者底层 Socket 发生异常
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            // 在指定长度内未匹配到 这里抛出请求头过大异常
            throw new RequestHeaderFieldsTooLargeException(e.getMessage());
        }
    }

}
