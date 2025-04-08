package cool.scx.http.x.http1.headers;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersImpl;
import cool.scx.http.x.http1.headers.connection.Connection;
import cool.scx.http.x.http1.headers.connection.ScxConnection;
import cool.scx.http.x.http1.headers.expect.ScxExpect;
import cool.scx.http.x.http1.headers.transfer_encoding.ScxTransferEncoding;
import cool.scx.http.x.http1.headers.upgrade.ScxUpgrade;

import static cool.scx.http.headers.HttpFieldName.*;

public class Http1Headers extends ScxHttpHeadersImpl {

    public Http1Headers(ScxHttpHeaders headers) {
        super(headers);
    }

    public Http1Headers() {

    }

    public ScxConnection connection() {
        var c = get(CONNECTION);
        return c != null ? ScxConnection.of(c) : null;
    }

    public Http1Headers connection(Connection connection) {
        return (Http1Headers) set(CONNECTION, connection.value());
    }


    public ScxTransferEncoding transferEncoding() {
        var c = get(TRANSFER_ENCODING);
        return c != null ? ScxTransferEncoding.of(c) : null;
    }

    public Http1Headers transferEncoding(ScxTransferEncoding transferEncoding) {
        return (Http1Headers) set(TRANSFER_ENCODING, transferEncoding.value());
    }

    public ScxExpect expect() {
        var c = get(EXPECT);
        return c != null ? ScxExpect.of(c) : null;
    }

    public Http1Headers expect(ScxExpect expect) {
        return (Http1Headers) set(EXPECT, expect.value());
    }

    public ScxUpgrade upgrade() {
        var c = get(UPGRADE);
        return c != null ? ScxUpgrade.of(c) : null;
    }

    public Http1Headers upgrade(ScxUpgrade upgrade) {
        return (Http1Headers) set(UPGRADE, upgrade.value());
    }

}
