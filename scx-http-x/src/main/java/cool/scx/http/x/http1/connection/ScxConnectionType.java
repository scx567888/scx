package cool.scx.http.x.http1.connection;

public sealed interface ScxConnectionType permits ConnectionType, ScxConnectionTypeImpl {

    static ScxConnectionType of(String v) {
        // 优先使用 ConnectionType
        var m = ConnectionType.find(v);
        return m != null ? m : new ScxConnectionTypeImpl(v);
    }

    String value();

}
