package cool.scx.http.version;

/// HttpVersion
/// 
/// 这里我们只保留流行的 Http 版本,较旧的版本如 HTTP/0.9 和 HTTP/1.0 不再做保留
///
/// @author scx567888
/// @version 0.0.1
public enum HttpVersion {

    HTTP_1_1("http/1.1"),
    HTTP_2("h2"),
    HTTP_3("h3");

    private final String value;

    HttpVersion(String value) {
        this.value = value;
    }

    /// @param version v
    /// @return 未找到时 抛出异常
    public static HttpVersion of(String version) {
        var h = find(version);
        if (h == null) {
            throw new IllegalArgumentException("Unsupported HTTP version: " + version);
        }
        return h;
    }

    /// @param version v
    /// @return 未找到时返回 null
    public static HttpVersion find(String version) {
        var upperCase = version.toLowerCase();
        return switch (upperCase) {
            case "http/1.1" -> HTTP_1_1;
            case "h2" -> HTTP_2;
            case "h3" -> HTTP_3;
            default -> null;
        };
    }

    /// 协议名称
    public String value() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }

}
