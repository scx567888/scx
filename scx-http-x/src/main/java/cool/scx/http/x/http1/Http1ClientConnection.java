package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.x.XHttpClientOptions;
import cool.scx.http.x.http1.chunked.HttpChunkedOutputStream;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.http.x.http1.status_line.Http1StatusLine;
import cool.scx.io.data_reader.PowerfulLinkedDataReader;
import cool.scx.io.data_supplier.InputStreamDataSupplier;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import static cool.scx.http.headers.HttpFieldName.HOST;
import static cool.scx.http.headers.ScxHttpHeadersHelper.encodeHeaders;
import static cool.scx.http.method.HttpMethod.GET;
import static cool.scx.http.x.http1.Http1Helper.CRLF_BYTES;
import static cool.scx.http.x.http1.headers.transfer_encoding.TransferEncoding.CHUNKED;
import static java.io.OutputStream.nullOutputStream;
import static java.nio.charset.StandardCharsets.UTF_8;

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
        // 1, 创建 请求行
        var requestLine = new Http1RequestLine(request.method(), request.uri());
        // 1.1 编码
        var requestLineStr = requestLine.encode();

        //复制一份头便于修改
        var requestHeaders = new Http1Headers(request.headers());

        //让用户能够设置头信息
        writer.beforeWrite(requestHeaders, ScxHttpHeaders.of());

        // 处理头相关
        //设置 HOST 头
        if (!requestHeaders.contains(HOST)) {
            requestHeaders.set(HOST, request.uri().host());
        }

        // 是否不需要响应体 todo 这里的检查过于简单了
        var hasBody = request.method() != GET;

        //如果需要响应体
        if (hasBody) {
            //没有长度 我们就设置为 分块传输
            if (requestHeaders.contentLength() == null) {
                requestHeaders.transferEncoding(CHUNKED);
            }
        }

        var requestHeaderStr = encodeHeaders(requestHeaders);

        //先写入请求行 请求头的内容
        try {
            var h = requestLineStr + "\r\n" + requestHeaderStr + "\r\n";
            dataWriter.write(h.getBytes(UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        // 只有明确表示 分块的时候才使用分块
        var useChunkedTransfer = requestHeaders.transferEncoding() == CHUNKED;

        OutputStream out;

        if (!hasBody) {
            out = nullOutputStream();
        } else if (useChunkedTransfer) {
            out = new HttpChunkedOutputStream(dataWriter);
        } else {
            out = dataWriter;
        }

        writer.write(out);

        return this;
    }

    public ScxHttpClientResponse waitResponse() {
        //1, 读取状态行
        var statusLine = readStatusLine();

        //2, 读取响应头
        var headers = readHeaders();

        //3, 读取响应体
        var bodyInputStream = readBodyInputStream(headers);

        return new Http1ClientResponse(statusLine, headers, bodyInputStream);
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
        return Http1Helper.readHeaders(dataReader, options.maxHeaderSize());
    }

    //todo 超出最大长度怎么办
    public InputStream readBodyInputStream(Http1Headers headers) {
        return Http1Helper.readBodyInputStream(headers, dataReader, options.maxPayloadSize());
    }

}
