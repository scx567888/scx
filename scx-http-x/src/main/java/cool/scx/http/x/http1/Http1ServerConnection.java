package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.exception.*;
import cool.scx.http.version.HttpVersion;
import cool.scx.http.x.XHttpServerOptions;
import cool.scx.http.x.http1.chunked.HttpChunkedDataSupplier;
import cool.scx.http.x.http1.exception.HttpVersionNotSupportedException;
import cool.scx.http.x.http1.exception.RequestHeaderFieldsTooLargeException;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.http.x.http1.request_line.Http1RequestLineHelper.InvalidHttpRequestLineException;
import cool.scx.http.x.http1.request_line.Http1RequestLineHelper.InvalidHttpVersion;
import cool.scx.io.data_reader.PowerfulLinkedDataReader;
import cool.scx.io.data_supplier.InputStreamDataSupplier;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;
import cool.scx.io.io_stream.DataReaderInputStream;
import cool.scx.io.io_stream.FixedLengthDataReaderInputStream;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.util.Arrays;
import java.util.function.Consumer;

import static cool.scx.http.headers.ScxHttpHeadersHelper.encodeHeaders;
import static cool.scx.http.headers.ScxHttpHeadersHelper.parseHeaders;
import static cool.scx.http.status.ScxHttpStatusHelper.getReasonPhrase;
import static cool.scx.http.x.http1.Http1Helper.*;
import static cool.scx.http.x.http1.headers.connection.ConnectionType.CLOSE;
import static java.lang.System.Logger.Level.TRACE;
import static java.lang.System.getLogger;

/// Http 1.1 连接处理器
///
/// @author scx567888
/// @version 0.0.1
public class Http1ServerConnection {

    private final static Logger LOGGER = getLogger(Http1ServerConnection.class.getName());

    public final ScxTCPSocket tcpSocket;
    public final XHttpServerOptions options;
    public final PowerfulLinkedDataReader dataReader;
    public final OutputStream dataWriter;

    private final Consumer<ScxHttpServerRequest> requestHandler;
    private boolean running;

    public Http1ServerConnection(ScxTCPSocket tcpSocket, XHttpServerOptions options, Consumer<ScxHttpServerRequest> requestHandler) {
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

                // 2.1 验证请求头
                validateHeaders(headers);

                // 3, 读取 请求体流
                var bodyInputStream = readBodyInputStream(headers);

                // 4, 处理 100-continue 临时请求
                var is100ContinueExpected = checkIs100ContinueExpected(headers);
                if (is100ContinueExpected) {
                    //如果自动响应 我们直接发送
                    if (options.autoRespond100Continue()) {
                        try {
                            Http1Helper.sendContinue100(dataWriter);
                        } catch (IOException e) {
                            throw new CloseConnectionException("Failed to write continue", e);
                        }
                    } else {
                        //否则交给用户去处理
                        bodyInputStream = new AutoContinueInputStream(bodyInputStream, dataWriter);
                    }
                }

                var body = new ScxHttpBodyImpl(bodyInputStream, headers);

                // 5, 判断是否为 WebSocket 握手请求 并创建对应请求
                var isWebSocketHandshake = checkIsWebSocketHandshake(requestLine, headers);

                var request = isWebSocketHandshake ?
                        new Http1ServerWebSocketHandshakeRequest(this, requestLine, headers, body) :
                        new Http1ServerRequest(this, requestLine, headers, body);

                try {
                    // 6, 调用用户处理器
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

    public void close() throws IOException {
        //关闭连接就表示 不需要继续读取了
        stop();
        //关闭连接
        tcpSocket.close();
    }

    private void _callRequestHandler(ScxHttpServerRequest request) {
        if (requestHandler != null) {
            requestHandler.accept(request);
        }
    }

    private Http1RequestLine readRequestLine() {
        //尝试读取 请求行
        try {
            var requestLineBytes = dataReader.readUntil(CRLF_BYTES, options.maxRequestLineSize());
            var requestLineStr = new String(requestLineBytes);
            return Http1RequestLine.of(requestLineStr);
        } catch (NoMoreDataException | UncheckedIOException e) {
            // Socket 关闭了 或者底层 Socket 发生异常
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            // 在指定长度内未匹配到 这里抛出 URI 过长异常
            throw new URITooLongException(e.getMessage());
        } catch (InvalidHttpRequestLineException e) {
            // 解析 RequestLine 异常
            throw new BadRequestException("Invalid HTTP request line : " + e.requestLineStr);
        } catch (InvalidHttpVersion e) {
            throw new HttpVersionNotSupportedException("Invalid HTTP version : " + e.versionStr);
        }
    }

    private Http1Headers readHeaders() {
        //尝试读取 headers
        try {
            // 有可能没有头 也就是说 请求行后直接跟着 \r\n , 这里检查一下
            var a = dataReader.peek(2);
            if (Arrays.equals(a, CRLF_BYTES)) {
                dataReader.skip(2);
                return new Http1Headers();
            }

            var headerBytes = dataReader.readUntil(CRLF_CRLF_BYTES, options.maxHeaderSize());
            var headerStr = new String(headerBytes);
            return parseHeaders(new Http1Headers(), headerStr);
        } catch (NoMoreDataException | UncheckedIOException e) {
            // Socket 关闭了 或者底层 Socket 发生异常
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            // 在指定长度内未匹配到 这里抛出请求头过大异常
            throw new RequestHeaderFieldsTooLargeException(e.getMessage());
        } catch (Exception e) {
            // 解析 Header 异常
            throw new BadRequestException(e.getMessage());
        }
    }

    //todo 这里如果 dataReader 抛出了 NoMoreDataException 我们需要处理
    private InputStream readBodyInputStream(Http1Headers headers) {
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
                throw new ContentTooLargeException();
            }
            return new FixedLengthDataReaderInputStream(dataReader, contentLength);
        }

        //3, 没有长度的空请求体
        return InputStream.nullInputStream();
    }

    private void handleHttpException(ScxHttpException e) {
        //todo 这个方法不是特别合理,
        // 不一定所有的 情况都需要关闭连接 是否可以在 ScxHttpException 中添加是否严重 或者根据状态码来区分 ?

        var status = e.status();
        var reasonPhrase = getReasonPhrase(status, "unknown");

        var sb = new StringBuilder();
        sb.append(HttpVersion.HTTP_1_1.value());
        sb.append(" ");
        sb.append(status.code());
        sb.append(" ");
        sb.append(reasonPhrase);
        sb.append("\r\n");

        var headers = new Http1Headers();
        // we are escaping the connection loop, the connection will be closed
        headers.connection(CLOSE);

        var message = reasonPhrase.getBytes();

        headers.contentLength(message.length);

        var headerStr = encodeHeaders(headers);

        sb.append(headerStr);
        sb.append("\r\n");


        try {
            dataWriter.write(sb.toString().getBytes());
            dataWriter.write(message);
        } catch (IOException ee) {
            LOGGER.log(TRACE, "发送请求错误时发生错误 !!!");
            stop();
        }

    }

}
