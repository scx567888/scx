package cool.scx.http.x.http1.headers;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersImpl;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.x.http1.headers.connection.Connection;
import cool.scx.http.x.http1.headers.connection.ScxConnection;
import cool.scx.http.x.http1.headers.transfer_encoding.ScxTransferEncoding;

import static cool.scx.http.headers.HttpFieldName.CONNECTION;
import static cool.scx.http.headers.HttpFieldName.TRANSFER_ENCODING;

public class Http1Headers extends ScxHttpHeadersImpl {

    public Http1Headers(ScxHttpHeaders headers) {
        super(headers);
    }

    public Http1Headers() {

    }

    public ScxTransferEncoding transferEncoding() {
        var c = get(TRANSFER_ENCODING);
        return c != null ? ScxTransferEncoding.of(c) : null;
    }

    public ScxConnection connection() {
        var c = get(CONNECTION);
        return c != null ? ScxConnection.of(c) : null;
    }

    public ScxHttpHeadersWritable transferEncoding(ScxTransferEncoding transferEncoding) {
        set(TRANSFER_ENCODING, transferEncoding.value());
        return this;
    }

    public ScxHttpHeadersWritable connection(Connection connection) {
        set(CONNECTION, connection.value());
        return this;
    }

}
