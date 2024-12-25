package cool.scx.http.usagi.http1x;

import cool.scx.http.*;
import cool.scx.http.exception.InternalServerErrorException;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.usagi.FixedLengthInputStream;
import cool.scx.http.usagi.HttpChunkedInputStream;
import cool.scx.http.usagi.UsagiHttpServerOptions;
import cool.scx.http.usagi.exception.CloseConnectionException;
import cool.scx.io.LinkedDataReader;
import cool.scx.io.InputStreamDataSupplier;
import cool.scx.io.NoMatchFoundException;
import cool.scx.io.NoMoreDataException;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

import static cool.scx.http.HttpFieldName.CONNECTION;
import static cool.scx.http.usagi.http1x.Http1xHelper.*;

/**
 * Http 1.x 连接处理器
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xConnection {

    private final static System.Logger LOGGER = System.getLogger(Http1xConnection.class.getName());

    private static final byte[] CRLF_BYTES = "\r\n".getBytes();
    private static final byte[] CRLF_CRLF_BYTES = "\r\n\r\n".getBytes();

    private final ScxTCPSocket tcpSocket;
    private final UsagiHttpServerOptions options;
    private final Consumer<ScxHttpServerRequest> requestHandler;
    private final LinkedDataReader dataReader;
    private final OutputStream dataWriter;

    public Http1xConnection(ScxTCPSocket tcpSocket, UsagiHttpServerOptions options, Consumer<ScxHttpServerRequest> requestHandler) {
        this.tcpSocket = tcpSocket;
        this.options = options;
        this.requestHandler = requestHandler;
        this.dataReader = new LinkedDataReader(new InputStreamDataSupplier(this.tcpSocket.inputStream()));
        this.dataWriter = this.tcpSocket.outputStream();
    }


    public void start() {
        System.out.println("新连接");
        while (true) {
            try {
                // 1, 读取 请求行
                var requestLine = readRequestLine();

                // 2, 读取 请求头 
                var headers = readHeaders();

                // 3, 读取 请求体
                var body = readBody(headers);

                // 4, 是否是持久连接
                var isKeepAlive = checkIsKeepAlive(requestLine, headers);

                // 4, 判断是否为 WebSocket 握手请求 并创建对应请求
                var isWebSocketHandshake = checkIsWebSocketHandshake(requestLine, headers);

                var request = isWebSocketHandshake ?
                        new Http1xServerWebSocketHandshakeRequest(requestLine, headers, body, tcpSocket, dataReader, dataWriter, isKeepAlive) :
                        new Http1xServerRequest(requestLine, headers, body, tcpSocket, dataReader, dataWriter, isKeepAlive);

                // 5, 调用用户处理器
                _callRequestHandler(request);

            } catch (CloseConnectionException e) {
                //这种情况是我们主动触发的, 表示需要关闭连接 这里直接跳出循环, 以便完成关闭
                break;
            } catch (ScxHttpException e) {
                handleHttpException(e);
            } catch (Throwable e) {
                handleHttpException(new InternalServerErrorException(e));
            }
        }
        // 循环结束则关闭连接
        try {
            tcpSocket.close();
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.TRACE, "关闭 Socket 时发生错误！", e);
        }
    }

    private void _callRequestHandler(Http1xServerRequest request) {
        if (requestHandler != null) {
            requestHandler.accept(request);
        }
    }

    private Http1xRequestLine readRequestLine() {
        try {
            var requestLineBytes = dataReader.readUntil(CRLF_BYTES, options.maxRequestLineSize());
            var requestLineStr = new String(requestLineBytes);
            return Http1xRequestLine.of(requestLineStr);
        } catch (NoMoreDataException e) {
            // Socket 关闭了
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            // 在指定长度内未匹配到 这里抛出 URI 过长异常
            throw new ScxHttpException(HttpStatusCode.URI_TOO_LONG, e.getMessage());
        } catch (IllegalArgumentException e) {
            // 解析 RequestLine 异常
            throw new ScxHttpException(HttpStatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    private ScxHttpHeadersWritable readHeaders() {
        try {
            var headerBytes = dataReader.readUntil(CRLF_CRLF_BYTES, options.maxHeaderSize());
            var headerStr = new String(headerBytes);
            return ScxHttpHeaders.of(headerStr);
        } catch (NoMoreDataException e) {
            // Socket 关闭了
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            // 在指定长度内未匹配到 这里抛出请求头过大异常
            throw new ScxHttpException(HttpStatusCode.REQUEST_HEADER_FIELDS_TOO_LARGE, e.getMessage());
        } catch (IllegalArgumentException e) {
            // 解析 Header 异常
            throw new ScxHttpException(HttpStatusCode.BAD_REQUEST, e.getMessage());
        }
    }

    private ScxHttpBody readBody(ScxHttpHeaders headers) {
        //1, 判断请求体是不是分块传输
        var isChunkedTransfer = checkIsChunkedTransfer(headers);
        if (isChunkedTransfer) {
            return new ScxHttpBodyImpl(new HttpChunkedInputStream(dataReader), headers, 65535);
        }

        //2, 判断请求体是不是有 长度
        var contentLength = headers.contentLength();
        if (contentLength != null) {
            return new ScxHttpBodyImpl(new FixedLengthInputStream(dataReader, contentLength), headers, 65536);
        }

        //3, 没有长度的空请求体
        return new ScxHttpBodyImpl(InputStream.nullInputStream(), headers, 65536);
    }

    public void stop() {
        //todo 
    }

    private void handleHttpException(ScxHttpException e) {

        var headers = ScxHttpHeaders.of();

        var sb = new StringBuilder();
        sb.append(HttpVersion.HTTP_1_1.value());
        sb.append(" ");
        sb.append(e.statusCode().code());
        sb.append(" ");
        sb.append(e.statusCode().description());
        sb.append("\r\n");

        // we are escaping the connection loop, the connection will be closed
        headers.set(CONNECTION, "close");

        var message = e.statusCode().description().getBytes();

        headers.contentLength(message.length);

        var headerStr = headers.encode();

        sb.append(headerStr);
        sb.append("\r\n");


        try {
            dataWriter.write(sb.toString().getBytes());
            dataWriter.write(message);
        } catch (IOException ee) {
            LOGGER.log(System.Logger.Level.TRACE, "Failed to write request exception");
        }

    }

}
