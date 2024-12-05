package cool.scx.http.usagi.http1x;

import cool.scx.http.HttpVersion;
import cool.scx.http.ScxHttpMethod;

/**
 * Http 1.x 的请求行
 *
 * @author scx567888
 * @version 0.0.1
 */
public record Http1xRequestLine(ScxHttpMethod method, String path, HttpVersion version) {

}
