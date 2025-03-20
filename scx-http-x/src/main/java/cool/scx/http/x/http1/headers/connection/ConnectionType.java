package cool.scx.http.x.http1.headers.connection;

public enum ConnectionType implements ScxConnectionType {

    KEEP_ALIVE("keep-alive"),
    CLOSE("close"),
    UPGRADE("upgrade");

    private final String value;

    ConnectionType(String value) {
        this.value = value;
    }

    public static ConnectionType find(String v) {
        var v1 = v.toLowerCase();
        //数量较少时 switch 性能要高于 Map
        return switch (v1) {
            case "keep-alive" -> KEEP_ALIVE;
            case "close" -> CLOSE;
            case "upgrade" -> UPGRADE;
            default -> null;
        };
    }

    @Override
    public String value() {
        return value;
    }

}
