package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.exception.InternalServerErrorException;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.x.XHttpServerOptions;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.io.data_reader.PowerfulLinkedDataReader;
import cool.scx.io.data_supplier.InputStreamDataSupplier;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.util.function.Consumer;

import static cool.scx.http.status.ScxHttpStatusHelper.getReasonPhrase;
import static cool.scx.http.x.http1.Http1Helper.*;
import static cool.scx.http.x.http1.Http1Reader.*;
import static cool.scx.http.x.http1.headers.connection.Connection.CLOSE;
import static cool.scx.http.x.http1.headers.expect.Expect.CONTINUE;
import static java.lang.System.Logger.Level.ERROR;
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

            //1, 我们先读取请求
            ScxHttpServerRequest request;
            try {
                request = readRequest();
            } catch (Throwable e) {
                //如果读取请求失败 我们将其理解为系统错误 不可恢复
                handlerSystemException(e);
                break;
            }

            //2, 交由用户处理器处理
            try {
                _callRequestHandler(request);
            } catch (Throwable e) {
                //用户处理器 错误 我们尝试恢复
                handlerUserException(e, request);
            } finally {
                //todo 这里如果  _callRequestHandler 中 异步读取 body 怎么办?

                // 6, 如果 还是 running 说明需要继续复用当前 tcp 连接,并进行下一次 Request 的读取
                if (running) {
                    // 7, 用户处理器可能没有消费完请求体 这里我们帮助消费用户未消费的数据
                    consumeInputStream(request.body().inputStream());
                }
            }

        }
    }

    private Http1ServerRequest readRequest() throws CloseConnectionException {
        // 1, 读取 请求行
        var requestLine = readRequestLine(dataReader, options.maxRequestLineSize());

        // 2, 读取 请求头 
        var headers = readHeaders(dataReader, options.maxHeaderSize());

        // 3, 读取 请求体流
        var bodyInputStream = readBodyInputStream(headers, dataReader, options.maxPayloadSize());

        // 4, 在交给用户处理器进行处理之前, 我们需要做一些预处理

        // 4.1, 验证 请求头
        if (options.validateHost()) {
            validateHost(headers);
        }

        // 4.2, 处理 100-continue 临时请求
        if (headers.expect() == CONTINUE) {
            //如果启用了自动响应 我们直接发送
            if (options.autoRespond100Continue()) {
                try {
                    sendContinue100(dataWriter);
                } catch (IOException e) {
                    throw new CloseConnectionException("Failed to write continue", e);
                }
            } else {
                //否则交给用户去处理
                bodyInputStream = new AutoContinueInputStream(bodyInputStream, dataWriter);
            }
        }

        // 5, 判断是否为 WebSocket 握手请求 并创建对应请求
        var isWebSocketHandshake = checkIsWebSocketHandshake(requestLine, headers);

        return isWebSocketHandshake ?
                new Http1ServerWebSocketHandshakeRequest(this, requestLine, headers, bodyInputStream) :
                new Http1ServerRequest(this, requestLine, headers, bodyInputStream);
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

    public void handlerSystemException(Throwable e) {
        //这个 request 对象 仅为了做响应 实际上 并不包含任何内容
        var fakeRequest = new Http1ServerRequest(this, new Http1RequestLine(ScxHttpMethod.of("unknow"), ScxURI.of()), new Http1Headers().connection(CLOSE), InputStream.nullInputStream());
        handlerException(e, fakeRequest.response());

        // 这里我们停止监听并关闭连接
        try {
            close();
        } catch (IOException _) {
            
        }
    }

    public void handlerUserException(Throwable e, ScxHttpServerRequest request) {
        handlerException(e, request.response());
    }

    private void handlerException(Throwable e, ScxHttpServerResponse response) {
        var httpException = e instanceof ScxHttpException h ? h : new InternalServerErrorException(e.getMessage());
        var status = httpException.status();
        var reasonPhrase = getReasonPhrase(status, "unknown");

        if (tcpSocket.isClosed()) {
            LOGGER.log(ERROR, "用户处理器 发生异常 !!!, 因为 Socket 已被关闭, 所以错误信息可能没有正确返回给客户端 !!!", e);
        } else if (response.isSent()) {
            //这里表示 响应对象已经被使用了 我们只能打印日志
            LOGGER.log(ERROR, "用户处理器 发生异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", e);
        } else {
            LOGGER.log(ERROR, "用户处理器 发生异常 !!!", e);
            try {
                response.status(status).send(reasonPhrase);
            } catch (Exception ex) {
                LOGGER.log(ERROR, "用户处理器 发生异常 !!!, 尝试响应给客户端时发生异常 !!!", ex);
            }
        }
    }

}
