package cool.scx.http.usagi;

import cool.scx.http.HttpVersion;
import cool.scx.http.ScxHttpMethod;

public record HttpRequestLine(ScxHttpMethod method, String path, HttpVersion version) {

}
