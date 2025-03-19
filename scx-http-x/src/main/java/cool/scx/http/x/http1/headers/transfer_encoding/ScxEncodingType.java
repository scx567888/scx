package cool.scx.http.x.http1.headers.transfer_encoding;

public sealed interface ScxEncodingType permits EncodingType, ScxEncodingTypeImpl {

    static ScxEncodingType of(String v) {
        // 优先使用 EncodingType
        var m = EncodingType.find(v);
        return m != null ? m : new ScxEncodingTypeImpl(v);
    }

    String value();

}
