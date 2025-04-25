package cool.scx.http.headers.content_encoding;

/// ContentEncoding
public enum ContentEncoding implements ScxContentEncoding {

    GZIP("gzip");

    private final String value;

    ContentEncoding(String value) {
        this.value = value;
    }

    /// @param v v
    /// @return 未找到抛出异常
    public static ContentEncoding of(String v) {
        //数量较少时 switch 性能要高于 Map
        var h = find(v);
        if (h == null) {
            throw new IllegalArgumentException("Unknown content-encoding: " + v);
        }
        return h;
    }

    /// @param v v
    /// @return 未找到返回 null
    public static ContentEncoding find(String v) {
        var lv = v.toLowerCase();
        //数量较少时 switch 性能要高于 Map
        return switch (lv) {
            case "gzip" -> GZIP;
            default -> null;
        };
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}
