package cool.scx.http.x.http1.headers;

import cool.scx.http.headers.ScxHttpHeaderName;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.x.http1.headers.connection.Connection;
import cool.scx.http.x.http1.headers.connection.ScxConnection;
import cool.scx.http.x.http1.headers.expect.ScxExpect;
import cool.scx.http.x.http1.headers.transfer_encoding.ScxTransferEncoding;
import cool.scx.http.x.http1.headers.upgrade.ScxUpgrade;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cool.scx.http.headers.HttpFieldName.*;

public class Http1Headers implements ScxHttpHeadersWritable {

    private final ScxHttpHeadersWritable h;

    public Http1Headers(ScxHttpHeadersWritable h) {
        this.h = ScxHttpHeaders.of(h);
    }

    public Http1Headers() {
        this.h = ScxHttpHeaders.of();
    }

    @Override
    public Http1Headers set(ScxHttpHeaderName name, String... value) {
        h.set(name, value);
        return this;
    }

    @Override
    public ScxHttpHeadersWritable add(ScxHttpHeaderName name, String... value) {
        h.add(name, value);
        return this;
    }

    @Override
    public ScxHttpHeadersWritable remove(ScxHttpHeaderName name) {
        h.remove(name);
        return this;
    }

    @Override
    public ScxHttpHeadersWritable clear() {
        h.clear();
        return this;
    }

    @Override
    public long size() {
        return h.size();
    }

    @Override
    public Set<ScxHttpHeaderName> names() {
        return h.names();
    }

    @Override
    public String get(ScxHttpHeaderName name) {
        return h.get(name);
    }

    @Override
    public List<String> getAll(ScxHttpHeaderName name) {
        return h.getAll(name);
    }

    @Override
    public Iterator<Map.Entry<ScxHttpHeaderName, List<String>>> iterator() {
        return h.iterator();
    }

    public ScxConnection connection() {
        var c = get(CONNECTION);
        return c != null ? ScxConnection.of(c) : null;
    }

    public Http1Headers connection(Connection connection) {
        return set(CONNECTION, connection.value());
    }

    public ScxTransferEncoding transferEncoding() {
        var c = get(TRANSFER_ENCODING);
        return c != null ? ScxTransferEncoding.of(c) : null;
    }

    public Http1Headers transferEncoding(ScxTransferEncoding transferEncoding) {
        return set(TRANSFER_ENCODING, transferEncoding.value());
    }

    public ScxExpect expect() {
        var c = get(EXPECT);
        return c != null ? ScxExpect.of(c) : null;
    }

    public Http1Headers expect(ScxExpect expect) {
        return set(EXPECT, expect.value());
    }

    public ScxUpgrade upgrade() {
        var c = get(UPGRADE);
        return c != null ? ScxUpgrade.of(c) : null;
    }

    public Http1Headers upgrade(ScxUpgrade upgrade) {
        return set(UPGRADE, upgrade.value());
    }

}
