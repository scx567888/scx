package cool.scx.http.x.http1;

import cool.scx.http.HttpVersion;
import cool.scx.http.ScxHttpMethod;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.uri.ScxURI;

import java.net.URLDecoder;

import static cool.scx.http.HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED;
import static cool.scx.http.HttpVersion.HTTP_1_1;
import static java.nio.charset.StandardCharsets.UTF_8;

final class Http1RequestLineHelper {

    public static Http1RequestLine parseRequestLine(String requestLineStr) {
        var parts = requestLineStr.split(" ");

        //如果长度等于 2, 则可能是 HTTP/0.9 请求 
        //如果长度大于 3, 则可能是 路径中包含意外的空格
        //但是此处我们没必要细化异常 所以全部抛出 400 异常
        if (parts.length != 3) {
            throw new BadRequestException("Invalid HTTP request line : " + requestLineStr);
        }

        var methodStr = parts[0];
        var pathStr = parts[1];
        var versionStr = parts[2];

        //尝试解码路径 如果解析失败, 则可能是路径中包含非法字符
        //但是此处我们同样没必要细化异常 所以全部抛出 400 异常
        String decodedPath;
        try {
            decodedPath = URLDecoder.decode(pathStr, UTF_8);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid HTTP request line : " + requestLineStr);
        }

        var method = ScxHttpMethod.of(methodStr);
        var path = ScxURI.of(decodedPath);
        var version = HttpVersion.find(versionStr);

        //这里我们强制 版本号必须是 HTTP/1.1
        if (version != HTTP_1_1) {
            throw new ScxHttpException(HTTP_VERSION_NOT_SUPPORTED, "Invalid HTTP version : " + versionStr);
        }

        return new Http1RequestLine(method, path, version);
    }

    public static String encodeRequestLine(Http1RequestLine requestLine) {
        var method = requestLine.method().value();
        var path = ScxURI.of(requestLine.path()).scheme(null).host(null).encode(true);
        var version = requestLine.version() != null ? requestLine.version().value() : HTTP_1_1.value();
        return method + " " + path + " " + version;
    }

}
