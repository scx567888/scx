package cool.scx.http;

/**
 * HttpVersion
 */
public enum HttpVersion {

    HTTP_1_1("HTTP/1.1"),

    HTTP_2("HTTP/2.0");

    private final String value;

    HttpVersion(String value) {
        this.value = value;
    }

    /**
     * @param version v
     * @return 未找到时 抛出异常
     */
    public static HttpVersion of(String version) {
        var upperCase = version.toUpperCase();
        return switch (upperCase) {
            case "HTTP/1.1" -> HTTP_1_1;
            case "HTTP/2.0" -> HTTP_2;
            default -> throw new IllegalArgumentException("Unsupported HTTP version: " + version);
        };
    }

    /**
     * @param version v
     * @return 未找到时返回 null
     */
    public static HttpVersion find(String version) {
        var upperCase = version.toUpperCase();
        return switch (upperCase) {
            case "HTTP/1.1" -> HTTP_1_1;
            case "HTTP/2.0" -> HTTP_2;
            default -> null;
        };
    }

    public String value() {
        return this.value;
    }

}
