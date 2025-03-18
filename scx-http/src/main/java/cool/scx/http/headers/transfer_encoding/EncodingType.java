package cool.scx.http.headers.transfer_encoding;

public enum EncodingType implements ScxEncodingType {

    CHUNKED("chunked"),
    GZIP("gzip"),
    COMPRESS("compress");

    private final String value;

    EncodingType(String value) {
        this.value = value;
    }

    /// @param v v
    /// @return 未找到抛出异常
    public static EncodingType of(String v) {
        //数量较少时 switch 性能要高于 Map
        var h = find(v);
        if (h == null) {
            throw new IllegalArgumentException("Unknown encoding type : " + v);
        }
        return h;
    }

    /// @param v v
    /// @return 未找到返回 null
    public static EncodingType find(String v) {
        var v1 = v.toLowerCase();
        //数量较少时 switch 性能要高于 Map
        return switch (v1) {
            case "chunked" -> CHUNKED;
            case "gzip" -> GZIP;
            case "compress" -> COMPRESS;
            default -> null;
        };
    }

    @Override
    public String value() {
        return value;
    }

}
