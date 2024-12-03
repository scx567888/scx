package cool.scx.http.usagi;

import cool.scx.http.HttpVersion;
import cool.scx.http.ScxHttpMethod;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public record HttpRequestLine(ScxHttpMethod method, String path, HttpVersion version) {

}
