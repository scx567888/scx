package cool.scx.http.usagi.http1x;

import cool.scx.http.*;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.http.usagi.UsagiHttpClientRequest;
import cool.scx.http.usagi.UsagiHttpClientResponse;
import cool.scx.io.DataReaderInputStream;
import cool.scx.io.FixedLengthDataReaderInputStream;
import cool.scx.io.InputStreamDataSupplier;
import cool.scx.io.PowerfulLinkedDataReader;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static cool.scx.http.HttpFieldName.TRANSFER_ENCODING;

public class Http1xClientConnection {

    private final ScxTCPSocket tcpSocket;
    private final PowerfulLinkedDataReader dataReader;
    private final OutputStream dataWriter;

    public Http1xClientConnection(ScxTCPSocket tcpSocket) {
        this.tcpSocket = tcpSocket;
        this.dataReader = new PowerfulLinkedDataReader(new InputStreamDataSupplier(tcpSocket.inputStream()));
        this.dataWriter = new NoCloseOutputStream(tcpSocket.outputStream());
    }

    public static String getPath(ScxURIWritable uri) {
        var encode = uri.scheme(null).host(null).port(-1).encode(true);
        return encode;
    }

    public Http1xClientConnection sendRequest(UsagiHttpClientRequest request, MediaWriter writer) {
        var sb = new StringBuilder();
        sb.append(request.method().value());
        sb.append(" ");
        sb.append(getPath(request.uri()));
        sb.append(" ");
        sb.append(HttpVersion.HTTP_1_1.value());
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
        var lineBytes = dataReader.readUntil("\r\n".getBytes());

        var line = new String(lineBytes);

        var linePart = line.split(" ", 3);

        if (linePart.length != 3) {
            throw new RuntimeException("Invalid usagi response: " + line);
        }

        var version = linePart[0];
        var code = Integer.parseInt(linePart[1]);
        var description = linePart[2];


        var headerBytes = dataReader.readUntil("\r\n\r\n".getBytes());
        var headerStr = new String(headerBytes);
        var headers = ScxHttpHeaders.of(headerStr);

        var status = HttpStatusCode.of(code);

        //此处判断请求体是不是分块传输
        var transferEncoding = headers.get(TRANSFER_ENCODING);
        ScxHttpBody body;

        if ("chunked".equals(transferEncoding)) {
            body = new ScxHttpBodyImpl(new DataReaderInputStream(new HttpChunkedDataSupplier(dataReader)), headers, 65535);
        } else {
            var contentLength = headers.contentLength();
            if (contentLength != null) {
                body = new ScxHttpBodyImpl(new FixedLengthDataReaderInputStream(dataReader, contentLength), headers, 65536);
            } else {
                body = new ScxHttpBodyImpl(InputStream.nullInputStream(), headers, 65536);
            }
        }

        return new UsagiHttpClientResponse(status, headers, body);

    }

}
