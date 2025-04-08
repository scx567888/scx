package cool.scx.http.uri;

import cool.scx.http.parameters.Parameters;

import java.net.URI;

import static cool.scx.http.uri.URIEncoder.encodeURI;

/// ScxURI
///
/// @author scx567888
/// @version 0.0.1
public interface ScxURI {

    /// 创建一个空的 ScxURI 实例
    static ScxURIWritable of() {
        return new ScxURIImpl();
    }

    /// 从一个原始的 URI 字符串创建 URI 实例。
    ///
    /// 此方法适用于传入的是未经 URI 编码的原始字符串（如直接从用户输入中获取），
    /// 内部会自动对其进行 URI 编码后再构造 ScxURI 实例。
    ///
    ///
    /// 示例：
    /// ```java
    /// ScxURI.of("http://xxxx.com/路径?a=空 格");
    /// // 内部会先编码为 http://xxxx.com/%E8%B7%AF%E5%BE%84?a=%E7%A9%BA+%E6%A0%BC
    ///```
    ///
    /// @param rawStr 原始未编码的 URI 字符串
    /// @return 编码后解析得到的 ScxURI
    static ScxURIWritable of(String rawStr) {
        return ofEncoded(encodeURI(rawStr));
    }

    /// 从一个已编码的标准 URI 字符串创建 URI 实例。
    ///
    /// 此方法假设传入的字符串已经是合法的 URI 编码格式，内部不会再次进行编码，直接解析为 ScxURI
    ///
    ///
    /// 示例：
    /// ```java
    /// ScxURI.ofEncoded("https://xxxx.com/%E8%B7%AF%E5%BE%84?a=%E7%A9%BA+%E6%A0%BC");
    ///```
    ///
    /// @param encodedStr 已编码的 URI 字符串
    /// @return 解析得到的 ScxURI
    static ScxURIWritable ofEncoded(String encodedStr) {
        return of(URI.create(encodedStr));
    }

    /// 从一个标准 URI 对象创建 URI 实例。
    ///
    /// @param u 标准 URI 对象
    /// @return 构造得到的 ScxURI
    static ScxURIWritable of(URI u) {
        return new ScxURIImpl()
                .scheme(u.getScheme())
                .host(u.getHost())
                .port(u.getPort() == -1 ? null : u.getPort())
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

    Integer port();

    String path();

    Parameters<String, String> query();

    String fragment();

    default String getQuery(String name) {
        return query().get(name);
    }

    /// 编码 (默认不进行 URI 编码)
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
        // 此处同样因为 URI.create 只能处理编码后的标准格式 所以先编码
        return URI.create(encode(true));
    }

}
