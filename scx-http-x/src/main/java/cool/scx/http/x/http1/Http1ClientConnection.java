package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpBody;
import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.exception.ContentTooLargeException;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.x.XHttpClientOptions;
import cool.scx.http.x.http1.chunked.HttpChunkedDataSupplier;
import cool.scx.http.x.http1.chunked.HttpChunkedOutputStream;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.http.x.http1.status_line.Http1StatusLine;
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
import java.util.Arrays;

import static cool.scx.http.headers.HttpFieldName.HOST;
import static cool.scx.http.headers.ScxHttpHeadersHelper.encodeHeaders;
import static cool.scx.http.headers.ScxHttpHeadersHelper.parseHeaders;
import static cool.scx.http.method.HttpMethod.GET;
import static cool.scx.http.x.http1.Http1Helper.*;
import static cool.scx.http.x.http1.headers.transfer_encoding.EncodingType.CHUNKED;
import static java.io.OutputStream.nullOutputStream;

public class Http1ClientConnection {

    public final ScxTCPSocket tcpSocket;
    public final PowerfulLinkedDataReader dataReader;
    public final OutputStream dataWriter;
    public final Http1ClientConnectionOptions options;

    public Http1ClientConnection(ScxTCPSocket tcpSocket, XHttpClientOptions options) {
        this.tcpSocket = tcpSocket;
        this.dataReader = new PowerfulLinkedDataReader(new InputStreamDataSupplier(tcpSocket.inputStream()));
        this.dataWriter = new NoCloseOutputStream(tcpSocket.outputStream());
        this.options = options.http1ConnectionOptions();
    }

    public Http1ClientConnection sendRequest(ScxHttpClientRequest request, MediaWriter writer) {
        //1,创建 请求头
        var requestLine = new Http1RequestLine(request.method(), request.uri());

        var requestLineStr = requestLine.encode();

        //复制一份请求头便于修改
        var requestHeaders = new Http1Headers(request.headers());

        //让用户能够设置头信息
        writer.beforeWrite(requestHeaders, ScxHttpHeaders.of());

        //设置 HOST 头
        if (!requestHeaders.contains(HOST)) {
            requestHeaders.set(HOST, request.uri().host());
        }

        var hasBody = true;
        //是否不需要响应体
        if (request.method() == GET) {
            hasBody = false;
        }

        //如果需要响应体
        if (hasBody) {
            //没有长度 我们就设置为 分块传输
            if (requestHeaders.contentLength() == null) {
                requestHeaders.transferEncoding(CHUNKED);
            }
        }

        var useChunkedTransfer = false;

        //判断是否需要分段传输
        if (checkIsChunkedTransfer(requestHeaders)) {
            useChunkedTransfer = true;
        }

        var requestHeaderStr = encodeHeaders(requestHeaders);

        //先写入请求行 请求头的内容
        try {
            var h = requestLineStr + "\r\n" + requestHeaderStr + "\r\n";
            dataWriter.write(h.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OutputStream out;

        if (!hasBody) {
            out = nullOutputStream();
        } else if (useChunkedTransfer) {
            out = new HttpChunkedOutputStream(dataWriter);
        } else {
            out = dataWriter;
        }

        //todo 此处功能和 Http1ServerResponse 重复是否需要抽取 此处 是否也需要 BufferedOutputStream 进行包装
        writer.write(out);

        return this;
    }

    public ScxHttpClientResponse waitResponse() {
        //1, 读取状态行
        var statusLine = readStatusLine();

        //2, 读取响应头
        var headers = readHeaders();

        //3, 读取响应体
        var body = readBody(headers);

        return new Http1ClientResponse(statusLine, headers, body);
    }

    public Http1StatusLine readStatusLine() {
        try {
            var statusLineBytes = dataReader.readUntil(CRLF_BYTES, options.maxStatusLineSize());
            var statusLineStr = new String(statusLineBytes);
            return Http1StatusLine.of(statusLineStr);
        } catch (NoMoreDataException e) {
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            //todo 未找到 这里应该抛出什么异常 ?
            throw new CloseConnectionException();
        }
    }

    public Http1Headers readHeaders() {
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
        } catch (NoMoreDataException e) {
            throw new CloseConnectionException();
        } catch (NoMatchFoundException e) {
            //todo 未找到 这里应该抛出什么异常 ?
            throw new CloseConnectionException();
        }
    }

    //todo 超出最大长度怎么办
    public ScxHttpBody readBody(Http1Headers headers) {
        var isChunkedTransfer = Http1Helper.checkIsChunkedTransfer(headers);
        if (isChunkedTransfer) {
            return new ScxHttpBodyImpl(new DataReaderInputStream(new HttpChunkedDataSupplier(dataReader, options.maxPayloadSize())), headers);
        }

        //2, 判断请求体是不是有 长度
        var contentLength = headers.contentLength();
        if (contentLength != null) {
            // 请求体长度过大 这里抛出异常
            if (contentLength > options.maxPayloadSize()) {
                throw new ContentTooLargeException();
            }
            return new ScxHttpBodyImpl(new FixedLengthDataReaderInputStream(dataReader, contentLength), headers);
        }

        //3, 没有长度的空请求体
        return new ScxHttpBodyImpl(InputStream.nullInputStream(), headers);
    }

}
