package cool.scx.http.uri;

import cool.scx.http.Parameters;

import java.net.URI;

import static cool.scx.http.uri.URIEncoder.encodeURI;

/// ScxURI
///
/// @author scx567888
/// @version 0.0.1
public interface ScxURI {

    static ScxURIWritable of() {
        return new ScxURIImpl();
    }

    /// 根据 字符串进行解码
    static ScxURIWritable of(String uri) {
        // URI.create 只能处理标准格式 在此处为了兼容用户传入的特殊字符 我们先编码一次
        return of(URI.create(encodeURI(uri)));
    }

    static ScxURIWritable of(URI u) {
        return new ScxURIImpl()
                .scheme(u.getScheme())
                .host(u.getHost())
                .port(u.getPort())
                .path(u.getPath())
                .query(u.getQuery())
                .fragment(u.getFragment());
    }

    static ScxURIWritable of(ScxURI u) {
        return new ScxURIImpl()
                .scheme(u.scheme())
                .host(u.host())
                .port(u.port())
                .path(u.path())
                .query(u.query())
                .fragment(u.fragment());
    }

    String scheme();

    String host();

    int port();

    String path();

    Parameters<String, String> query();

    String fragment();

    default String getQuery(String name) {
        return query().get(name);
    }

    /// 编码 (默认不进行转换)
    ///
    /// @return 编码结果
    default String encode() {
        return encode(false);
    }

    /// 编码
    ///
    /// @param uriEncoding 是否进行 URI 编码
    /// @return 编码结果
    default String encode(boolean uriEncoding) {
        return ScxURIHelper.encodeScxURI(this, uriEncoding);
    }

    default URI toURI() {
        return URI.create(encode(true));
    }

}
