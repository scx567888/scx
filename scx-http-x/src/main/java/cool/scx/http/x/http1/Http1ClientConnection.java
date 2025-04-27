package cool.scx.http.x.http1;

import cool.scx.http.ScxHttpClientRequest;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.x.XHttpClientOptions;
import cool.scx.http.x.http1.chunked.HttpChunkedOutputStream;
import cool.scx.http.x.http1.headers.Http1Headers;
import cool.scx.http.x.http1.request_line.Http1RequestLine;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_reader.LinkedDataReader;
import cool.scx.io.data_supplier.InputStreamDataSupplier;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import static cool.scx.http.headers.HttpFieldName.HOST;
import static cool.scx.http.x.http1.Http1Helper.checkRequestHasBody;
import static cool.scx.http.x.http1.Http1Reader.*;
import static cool.scx.http.x.http1.headers.transfer_encoding.TransferEncoding.CHUNKED;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Http1ClientConnection {

    public final ScxTCPSocket tcpSocket;
    public final DataReader dataReader;
    public final OutputStream dataWriter;
    public final Http1ClientConnectionOptions options;

    public Http1ClientConnection(ScxTCPSocket tcpSocket, XHttpClientOptions options) {
        this.tcpSocket = tcpSocket;
        this.dataReader = new LinkedDataReader(new InputStreamDataSupplier(tcpSocket.inputStream()));
        this.dataWriter = new NoCloseOutputStream(tcpSocket.outputStream());
        this.options = options.http1ConnectionOptions();
    }

    public Http1ClientConnection sendRequest(ScxHttpClientRequest request, MediaWriter writer) {
        //复制一份头便于修改
        var headers = new Http1Headers(request.headers());

        //让用户设置头信息
        var expectedLength = writer.beforeWrite(headers, ScxHttpHeaders.of());

        // 1, 创建 请求行
        var requestLine = new Http1RequestLine(request.method(), request.uri());

        var requestLineStr = requestLine.encode();

        // 处理头相关
        // 1, 处理 HOST 相关
        if (!headers.contains(HOST)) {
            headers.set(HOST, request.uri().host());
        }

        // 2, 处理 body 相关
        if (expectedLength < 0) {//表示不知道 body 的长度
            // 如果用户已经手动设置了 Content-Length, 我们便不再设置 分块传输
            if (headers.contentLength() == null) {
                headers.transferEncoding(CHUNKED);
            }
        } else if (expectedLength > 0) {//拥有指定长度的响应体
            // 如果用户已经手动设置 分块传输, 我们便不再设置 Content-Length
            if (headers.transferEncoding() != CHUNKED) {
                headers.contentLength(expectedLength);
            }
        } else {
            // body 长度为 0 时 , 分两种情况
            // 1, 是需要明确写入 Content-Length : 0 的
            // 2, 是不需要写入任何长度相关字段
            var hasBody = checkRequestHasBody(request.method());
            if (hasBody) {
                // 这里同上, 进行分块传输判断
                if (headers.transferEncoding() != CHUNKED) {
                    headers.contentLength(expectedLength);
                }
            }
        }

        var requestHeaderStr = headers.encode();

        //先写入头部内容
        try {
            var h = requestLineStr + "\r\n" + requestHeaderStr + "\r\n";
            dataWriter.write(h.getBytes(UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        // 只有明确表示 分块的时候才使用分块
        var useChunkedTransfer = headers.transferEncoding() == CHUNKED;

        var out = useChunkedTransfer ? new HttpChunkedOutputStream(dataWriter) : dataWriter;

        try {
            writer.write(out);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return this;
    }

    public ScxHttpClientResponse waitResponse() {
        //1, 读取状态行
        var statusLine = readStatusLine(dataReader, options.maxStatusLineSize());

        //2, 读取响应头
        var headers = readHeaders(dataReader, options.maxHeaderSize());

        //3, 读取响应体 todo 超出最大长度怎么办
        var bodyInputStream = readBodyInputStream(headers, dataReader, options.maxPayloadSize());

        return new Http1ClientResponse(statusLine, headers, bodyInputStream);
    }

}
