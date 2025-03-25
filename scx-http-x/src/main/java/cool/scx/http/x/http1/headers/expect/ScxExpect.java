package cool.scx.http.x.http1.headers.expect;

public interface ScxExpect {

    static ScxExpect of(String v) {
        // 优先使用 TransferEncoding
        var m = Expect.find(v);
        return m != null ? m : new ScxExpectImpl(v);
    }

    String value();

}
