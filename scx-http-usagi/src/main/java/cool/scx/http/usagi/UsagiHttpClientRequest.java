package cool.scx.http.usagi;

import cool.scx.http.*;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.uri.ScxURIWritable;
import cool.scx.io.InputStreamDataSupplier;
import cool.scx.io.LinkedDataReader;
import cool.scx.tcp.ClassicTCPClient;
import cool.scx.tcp.NioTCPClient;
import cool.scx.tcp.ScxTCPClient;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.tcp.tls.TLS;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import static cool.scx.http.HttpFieldName.TRANSFER_ENCODING;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiHttpClientRequest extends ScxHttpClientRequestBase {

    private final UsagiHttpClient httpClient;

    ScxTCPClient tcpClient;
    ScxTCPSocket connect;

    public UsagiHttpClientRequest(UsagiHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public static String getPath(ScxURIWritable uri) {
        var encode = uri.scheme(null).host(null).port(-1).encode(true);
        return encode;
    }

    public static InetSocketAddress getRemoteAddress(ScxURI uri) {
        var defaultPort = -1;
        if ("http".equals(uri.scheme())) {
            defaultPort = 80;
        } else if ("https".equals(uri.scheme())) {
            defaultPort = 443;
        } else {
            throw new IllegalArgumentException("Unsupported scheme: " + uri.scheme());
        }
        var port = uri.port() == -1 ? defaultPort : uri.port();

        return new InetSocketAddress(uri.host(), port);
    }

    private static ScxHttpClientResponse waitResponse(InputStream in) {

        var dataReader = new LinkedDataReader(new InputStreamDataSupplier(in));

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
            body = new ScxHttpBodyImpl(new HttpChunkedInputStream(dataReader), headers, 65535);
        } else {
            var contentLength = headers.contentLength();
            if (contentLength != null) {
                body = new ScxHttpBodyImpl(new FixedLengthInputStream(dataReader, contentLength), headers, 65536);
            } else {
                body = new ScxHttpBodyImpl(InputStream.nullInputStream(), headers, 65536);
            }
        }

        return new UsagiHttpClientResponse(status, headers, body);
    }

    private static boolean checkIsHttps(ScxURIWritable uri) {
        if ("http".equals(uri.scheme())) {
            return false;
        } else if ("https".equals(uri.scheme())) {
            return true;
        } else {
            throw new IllegalArgumentException("Unsupported scheme: " + uri.scheme());
        }
    }

    public static TLS getTrustAllTLS() {
        // 创建自定义 TrustManager，忽略证书验证（仅用于测试环境）
        var trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        try {
            // 初始化 SSLContext
            var sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            return new TLS(sslContext);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ScxHttpClientResponse send(MediaWriter writer) {
        var isHttps = checkIsHttps(uri);

        var options = isHttps ? new UsagiHttpClientOptions().tls(getTrustAllTLS()) : httpClient.options;

        this.tcpClient = switch (options.tcpClientType()) {
            case CLASSIC -> new ClassicTCPClient(options);
            case NIO -> new NioTCPClient(options);
        };

        var remoteAddress = getRemoteAddress(uri);
        this.connect = tcpClient.connect(remoteAddress);

        var out = new NoCloseOutputStream(connect.outputStream());
        var in = connect.inputStream();

        var sb = new StringBuilder();
        sb.append(method.value());
        sb.append(" ");
        sb.append(getPath(uri));
        sb.append(" ");
        sb.append(HttpVersion.HTTP_1_1.value());
        sb.append("\r\n");

        //让用户能够设置头信息
        writer.beforeWrite(headers, ScxHttpHeaders.of());

        var headerStr = headers.encode();

        sb.append(headerStr);
        sb.append("\r\n");

        //先写入头 然后写入内容
        try {
            out.write(sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        writer.write(out);

        //等待响应
        return waitResponse(in);

    }


}
