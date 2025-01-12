package cool.scx.http.x.http1x;

import cool.scx.http.*;
import cool.scx.http.exception.InternalServerErrorException;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.http.x.XHttpServerOptions;
import cool.scx.http.x.web_socket.ServerWebSocket;
import cool.scx.io.*;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.util.function.Consumer;

import static cool.scx.http.HttpFieldName.CONNECTION;
import static cool.scx.http.HttpFieldName.EXPECT;
import static cool.scx.http.x.http1x.Http1xHelper.*;
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
    public final XHttpServerOptions options;
    public final PowerfulLinkedDataReader dataReader;
    public final OutputStream dataWriter;

    private final Consumer<ScxHttpServerRequest> requestHandler;
    private final Consumer<ScxServerWebSocket> webSocketHandler;
    private boolean running;

    public Http1xServerConnection(ScxTCPSocket tcpSocket, XHttpServerOptions options, Consumer<ScxHttpServerRequest> requestHandler, Consumer<ScxServerWebSocket> webSocketHandler) {
        this.tcpSocket = tcpSocket;
        this.options = options;
        this.requestHandler = requestHandler;
        //todo 这种方式暂时为了兼容 后期需要移除
        this.webSocketHandler = webSocketHandler;
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

                // Expect: 100-continue //todo 这里需要重构
                if ("100-continue".equalsIgnoreCase(headers.get(EXPECT))) {
                    try {
                        dataWriter.write(CONTINUE_100);
                    } catch (IOException e) {
                        throw new CloseConnectionException("Failed to write continue", e);
                    }
                }

                // 3, 读取 请求体流
                var bodyInputStream = readBodyInputStream(headers);

                var body = new ScxHttpBodyImpl(bodyInputStream, headers, 65535);

                // 4, 判断是否为 WebSocket 握手请求 并创建对应请求
                var isWebSocketHandshake = checkIsWebSocketHandshake(requestLine, headers);

                var request = isWebSocketHandshake ?
                        new Http1xServerWebSocketHandshakeRequest(this, requestLine, headers, body) :
                        new Http1xServerRequest(this, requestLine, headers, body);

                try {
                    // 5, 调用用户处理器
                    _callRequestHandler(request);
                } finally {
                    //todo 这里如果  _callRequestHandler 中 异步读取 body 怎么办?

                    // 6, 如果 还是 running 说明需要继续复用当前 tcp 连接,并进行下一次 Request 的读取
                    if (running) {
                        // 7, 用户处理器可能没有消费完请求体 这里我们帮助消费用户未消费的数据
                        consumeInputStream(bodyInputStream);
                    }
                }

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
        //todo 临时方案
        if (request instanceof Http1xServerWebSocketHandshakeRequest webSocketHandshakeRequest) {
            ServerWebSocket serverWebSocket = webSocketHandshakeRequest.webSocket();
            if (webSocketHandler != null) {
                webSocketHandler.accept(serverWebSocket);
            }
            serverWebSocket.start();
        } else {
            if (requestHandler != null) {
                requestHandler.accept(request);
            }
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
    private InputStream readBodyInputStream(ScxHttpHeaders headers) {
        // http1.1 本质上只有两种请求体格式 1, 分块传输 2, 指定长度 (当然也可以没有长度 那就表示没有请求体)

        //1, 判断请求体是不是分块传输
        var isChunkedTransfer = checkIsChunkedTransfer(headers);
        if (isChunkedTransfer) {
            return new DataReaderInputStream(new HttpChunkedDataSupplier(dataReader, options.maxPayloadSize()));
        }

        //2, 判断请求体是不是有 长度
        var contentLength = headers.contentLength();
        if (contentLength != null) {
            // 请求体长度过大 这里抛出异常
            if (contentLength > options.maxPayloadSize()) {
                throw new ScxHttpException(HttpStatusCode.CONTENT_TOO_LARGE);
            }
            return new FixedLengthDataReaderInputStream(dataReader, contentLength);
        }

        //3, 没有长度的空请求体
        return InputStream.nullInputStream();
    }

    private void handleHttpException(ScxHttpException e) {
        //todo 这个方法不是特别合理
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

    public void consumeInputStream(InputStream inputStream) throws IOException {
        try (inputStream) {
            inputStream.transferTo(OutputStream.nullOutputStream());
        }
    }

}
