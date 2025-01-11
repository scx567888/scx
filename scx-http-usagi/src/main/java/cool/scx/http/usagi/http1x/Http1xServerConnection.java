package cool.scx.http.usagi.http1x;

import cool.scx.http.*;
import cool.scx.http.exception.InternalServerErrorException;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.usagi.UsagiHttpServerOptions;
import cool.scx.io.*;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.util.function.Consumer;

import static cool.scx.http.HttpFieldName.CONNECTION;
import static cool.scx.http.usagi.http1x.Http1xHelper.*;
import static java.lang.System.Logger.Level.TRACE;
import static java.lang.System.getLogger;

/**
 * Http 1.x 连接处理器
 *
 * @author scx567888
 * @version 0.0.1
 */
public class Http1xServerConnection {

    private final static Logger LOGGER = getLogger(Http1xServerConnection.class.getName());

    public final ScxTCPSocket tcpSocket;
    public final UsagiHttpServerOptions options;
    public final PowerfulLinkedDataReader dataReader;
    public final OutputStream dataWriter;

    private final Consumer<ScxHttpServerRequest> requestHandler;
    private boolean running;

    public Http1xServerConnection(ScxTCPSocket tcpSocket, UsagiHttpServerOptions options, Consumer<ScxHttpServerRequest> requestHandler) {
        this.tcpSocket = tcpSocket;
        this.options = options;
        this.requestHandler = requestHandler;
        this.dataReader = new PowerfulLinkedDataReader(new InputStreamDataSupplier(this.tcpSocket.inputStream()));
        this.dataWriter = this.tcpSocket.outputStream();
        this.running = true;
    }

    public void start() {
        //开始读取 Http 请求
        while (running) {
            try {
                // 1, 读取 请求行
                var requestLine = readRequestLine();

                // 2, 读取 请求头 
                var headers = readHeaders();

                // 3, 读取 请求体
                var body = readBody(headers);

                // 4, 判断是否为 WebSocket 握手请求 并创建对应请求
                var isWebSocketHandshake = checkIsWebSocketHandshake(requestLine, headers);

                var request = isWebSocketHandshake ?
                        new Http1xServerWebSocketHandshakeRequest(this, requestLine, headers, body) :
                        new Http1xServerRequest(this, requestLine, headers, body);

                // 5, 调用用户处理器
                _callRequestHandler(request);

            } catch (CloseConnectionException e) {
                //这种情况是我们主动触发的, 表示需要关闭连接 这里直接跳出循环, 以便完成关闭
                stop();
            } catch (ScxHttpException e) {
                handleHttpException(e);
            } catch (Throwable e) {
                handleHttpException(new InternalServerErrorException(e));
            }
        }
    }

    public void stop() {
        //停止读取 http 请求
        running = false;
    }

    private void _callRequestHandler(Http1xServerRequest request) {
        if (requestHandler != null) {
            requestHandler.accept(request);
        }
    }

    private Http1xRequestLine readRequestLine() {
        //尝试读取 请求行
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
        //尝试读取 headers
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

    //todo 这里如果 dataReader 抛出了 NoMoreDataException 我们需要处理
    private ScxHttpBody readBody(ScxHttpHeaders headers) {
        // http1.1 本质上只有两种请求体格式 1, 分块传输 2, 指定长度 (当然也可以没有长度 那就表示没有请求体)

        //1, 判断请求体是不是分块传输
        var isChunkedTransfer = checkIsChunkedTransfer(headers);
        if (isChunkedTransfer) {
            return new ScxHttpBodyImpl(new DataReaderInputStream(new HttpChunkedDataSupplier(dataReader, options.maxPayloadSize())), headers, 65535);
        }

        //2, 判断请求体是不是有 长度
        var contentLength = headers.contentLength();
        if (contentLength != null) {
            // 请求体长度过大 这里抛出异常
            if (contentLength > options.maxPayloadSize()) {
                throw new ScxHttpException(HttpStatusCode.CONTENT_TOO_LARGE);
            }
            return new ScxHttpBodyImpl(new FixedLengthDataReaderInputStream(dataReader, contentLength), headers, 65536);
        }

        //3, 没有长度的空请求体
        return new ScxHttpBodyImpl(InputStream.nullInputStream(), headers, 65536);
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
            LOGGER.log(TRACE, "发送请求错误时发生错误 !!!");
        }

    }

}
