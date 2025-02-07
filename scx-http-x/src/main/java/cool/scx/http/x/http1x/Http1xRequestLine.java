package cool.scx.http.x.http1x;

import cool.scx.http.HttpVersion;
import cool.scx.http.ScxHttpMethod;

/**
 * Http 1.x 的请求行
 *
 * @param method
 * @param path    注意这里都是未进行 URL 编码的格式
 * @param version
 * @author scx567888
 * @version 0.0.1
 */
public record Http1xRequestLine(ScxHttpMethod method, String path, HttpVersion version) {

    public static Http1xRequestLine of(String requestLineStr) {
        return Http1xRequestLineHelper.parseRequestLine(requestLineStr);
    }

    public String encode() {
        return Http1xRequestLineHelper.encodeRequestLine(this);
    }

}
