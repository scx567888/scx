package cool.scx.http.x.http1x;

import cool.scx.http.*;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.x.XHttpClientOptions;
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

import static cool.scx.http.HttpFieldName.HOST;
import static cool.scx.http.HttpVersion.HTTP_1_1;
import static cool.scx.http.x.http1x.Http1xHelper.CRLF_BYTES;
import static cool.scx.http.x.http1x.Http1xHelper.CRLF_CRLF_BYTES;

public class Http1xClientConnection {

    public final ScxTCPSocket tcpSocket;
    public final PowerfulLinkedDataReader dataReader;
    public final OutputStream dataWriter;
    public final Http1xClientConnectionOptions options;

    public Http1xClientConnection(ScxTCPSocket tcpSocket, XHttpClientOptions options) {
        this.tcpSocket = tcpSocket;
        this.dataReader = new PowerfulLinkedDataReader(new InputStreamDataSupplier(tcpSocket.inputStream()));
        this.dataWriter = new NoCloseOutputStream(tcpSocket.outputStream());
        this.options = options.http1xConnectionOptions();
    }

    public Http1xClientConnection sendRequest(ScxHttpClientRequest request, MediaWriter writer) {
        //1,创建 请求头
        var requestLine = new Http1xRequestLine(request.method(), request.uri().path(), request.version() != null ? request.version() : HTTP_1_1);

        var requestLineStr = requestLine.encode();

        //复制一份请求头便于修改
        var requestHeaders = ScxHttpHeaders.of(request.headers());

        //让用户能够设置头信息
        writer.beforeWrite(requestHeaders, ScxHttpHeaders.of());

        //设置 HOST 头
        if (!requestHeaders.contains(HOST)) {
            requestHeaders.set(HOST, request.uri().host());
        }

        var requestHeaderStr = requestHeaders.encode();

        //先写入请求行 请求头的内容
        try {
            var h = requestLineStr + "\r\n" + requestHeaderStr + "\r\n";
            dataWriter.write(h.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //写入请求体的内容
        writer.write(dataWriter);

        return this;
    }

    public ScxHttpClientResponse waitResponse() {
        //1, 读取状态行
        var statusLine = readStatusLine();

        //2, 读取响应头
        var headers = readHeaders();

        //3, 读取响应体
        var body = readBody(headers);

        return new Http1xClientResponse(statusLine, headers, body);
    }

    public Http1xStatusLine readStatusLine() {
        try {
            var statusLineBytes = dataReader.readUntil(CRLF_BYTES, options.maxStatusLineSize());
            var statusLineStr = new String(statusLineBytes);
            return Http1xStatusLine.of(statusLineStr);
        } catch (NoMoreDataException e) {
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            //todo 未找到 这里应该抛出什么异常 ?
            throw new CloseConnectionException();
        }
    }

    public ScxHttpHeadersWritable readHeaders() {
        try {
            var headerBytes = dataReader.readUntil(CRLF_CRLF_BYTES, options.maxHeaderSize());
            var headerStr = new String(headerBytes);
            return ScxHttpHeaders.of(headerStr);
        } catch (NoMoreDataException e) {
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            //todo 未找到 这里应该抛出什么异常 ?
            throw new CloseConnectionException();
        }
    }

    //todo 超出最大长度怎么办
    public ScxHttpBody readBody(ScxHttpHeaders headers) {
        var isChunkedTransfer = Http1xHelper.checkIsChunkedTransfer(headers);
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

}
