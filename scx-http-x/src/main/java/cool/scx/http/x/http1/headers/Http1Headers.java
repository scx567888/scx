package cool.scx.http.x.http1.headers;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersImpl;
import cool.scx.http.headers.ScxHttpHeadersWritable;
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

    public ScxHttpHeadersWritable connection(Connection connection) {
        set(CONNECTION, connection.value());
        return this;
    }


    public ScxTransferEncoding transferEncoding() {
        var c = get(TRANSFER_ENCODING);
        return c != null ? ScxTransferEncoding.of(c) : null;
    }

    public ScxHttpHeadersWritable transferEncoding(ScxTransferEncoding transferEncoding) {
        set(TRANSFER_ENCODING, transferEncoding.value());
        return this;
    }

    public ScxExpect expect() {
        var c = get(EXPECT);
        return c != null ? ScxExpect.of(c) : null;
    }

    public ScxHttpHeadersWritable expect(ScxExpect expect) {
        set(EXPECT, expect.value());
        return this;
    }

    public ScxUpgrade upgrade() {
        var c = get(UPGRADE);
        return c != null ? ScxUpgrade.of(c) : null;
    }

    public ScxHttpHeadersWritable upgrade(ScxUpgrade upgrade) {
        set(UPGRADE, upgrade.value());
        return this;
    }

}
