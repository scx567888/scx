package cool.scx.http.x.http1x;

import cool.scx.http.*;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.x.XHttpClientOptions;
import cool.scx.io.*;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    public static String getPath(ScxURIWritable uri) {
        var encode = uri.scheme(null).host(null).port(-1).encode(true);
        return encode;
    }

    public Http1xClientConnection sendRequest(ScxHttpClientRequest request, MediaWriter writer) {
        var sb = new StringBuilder();
        sb.append(request.method().value());
        sb.append(" ");
        sb.append(getPath(request.uri()));
        sb.append(" ");
        sb.append(request.version().value());
        sb.append("\r\n");

        //让用户能够设置头信息
        writer.beforeWrite(request.headers(), ScxHttpHeaders.of());

        var headerStr = request.headers().encode();

        sb.append(headerStr);
        sb.append("\r\n");

        //先写入头 然后写入内容
        try {
            dataWriter.write(sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
