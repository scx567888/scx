package cool.scx.http.x.http1.request_line;

import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.version.HttpVersion;

import java.net.URI;

import static cool.scx.http.version.HttpVersion.HTTP_1_1;

public final class Http1RequestLineHelper {

    /// 解析 请求行
    public static Http1RequestLine parseRequestLine(String requestLineStr) throws InvalidHttpVersion, InvalidHttpRequestLineException {
        var parts = requestLineStr.split(" ");

        //如果长度等于 2, 则可能是 HTTP/0.9 请求 
        //如果长度大于 3, 则可能是 路径中包含意外的空格
        //但是此处我们没必要细化异常 全部抛出 InvalidHttpRequestLineException 异常
        if (parts.length != 3) {
            throw new InvalidHttpRequestLineException(requestLineStr);
        }

        var methodStr = parts[0];
        var pathStr = parts[1];
        var versionStr = parts[2];

        //尝试解码路径 如果解析失败, 则可能是路径中包含非法字符
        //此处我们同样不去细化异常 直接抛出 InvalidHttpRequestLineException 异常
        URI decodedPath;
        try {
            decodedPath = URI.create(pathStr);
        } catch (IllegalArgumentException e) {
            throw new InvalidHttpRequestLineException(requestLineStr);
        }

        var method = ScxHttpMethod.of(methodStr);
        var path = ScxURI.of(decodedPath);
        var version = HttpVersion.find(versionStr);

        //这里我们强制 版本号必须是 HTTP/1.1 , 这里需要细化一下 异常
        if (version != HTTP_1_1) {
            throw new InvalidHttpVersion(versionStr);
        }

        return new Http1RequestLine(method, path, version);
    }

    /// 编码请求行
    public static String encodeRequestLine(Http1RequestLine requestLine) {
        var methodStr = requestLine.method().value();
        //HTTP 路径我们不允许携带 协议和主机 这里通过创建一个新的 ScxURI 来进行移除
        var pathStr = ScxURI.of(requestLine.path()).scheme(null).host(null).encode(true);
        //此处我们强制使用 HTTP/1.1 , 忽略 requestLine 的版本号
        var versionStr = HTTP_1_1.value();
        //拼接返回
        return methodStr + " " + pathStr + " " + versionStr;
    }

}
