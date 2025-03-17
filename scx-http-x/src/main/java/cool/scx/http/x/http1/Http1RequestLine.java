package cool.scx.http.x.http1;

import cool.scx.http.version.HttpVersion;
import cool.scx.http.method.ScxHttpMethod;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.x.http1.Http1RequestLineHelper.InvalidHttpRequestLineException;
import cool.scx.http.x.http1.Http1RequestLineHelper.InvalidHttpVersion;

import static cool.scx.http.version.HttpVersion.HTTP_1_1;

/// Http 1.x 的请求行
///
/// @param method
/// @param path    注意这里都是未进行 URL 编码的格式
/// @param version
/// @author scx567888
/// @version 0.0.1
public record Http1RequestLine(ScxHttpMethod method, ScxURI path, HttpVersion version) {

    public Http1RequestLine(ScxHttpMethod method, ScxURI path) {
        this(method, path, HTTP_1_1);
    }

    public static Http1RequestLine of(String requestLineStr) throws InvalidHttpRequestLineException, InvalidHttpVersion {
        return Http1RequestLineHelper.parseRequestLine(requestLineStr);
    }

    public String encode() {
        return Http1RequestLineHelper.encodeRequestLine(this);
    }

}
