package cool.scx.http.x.http1.headers.expect;

public enum Expect implements ScxExpect {

    CONTINUE("100-continue");

    private final String value;

    Expect(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }


    /// @param v v
    /// @return 未找到抛出异常
    public static Expect of(String v) {
        //数量较少时 switch 性能要高于 Map
        var h = find(v);
        if (h == null) {
            throw new IllegalArgumentException("Unknown expect : " + v);
        }
        return h;
    }

    /// @param v v
    /// @return 未找到返回 null
    public static Expect find(String v) {
        var v1 = v.toLowerCase();
        //数量较少时 switch 性能要高于 Map
        return switch (v1) {
            case "100-continue" -> CONTINUE;
            default -> null;
        };
    }

}
