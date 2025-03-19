package cool.scx.http.x.http1;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersImpl;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.x.http1.connection.Connection;
import cool.scx.http.x.http1.connection.ConnectionType;
import cool.scx.http.x.http1.transfer_encoding.ScxEncodingType;
import cool.scx.http.x.http1.transfer_encoding.TransferEncoding;

import static cool.scx.http.headers.HttpFieldName.CONNECTION;
import static cool.scx.http.headers.HttpFieldName.TRANSFER_ENCODING;

public class Http1Headers extends ScxHttpHeadersImpl {

    public Http1Headers(ScxHttpHeaders headers) {
        super(headers);
    }

    public Http1Headers() {

    }

    public TransferEncoding transferEncoding() {
        var c = get(TRANSFER_ENCODING);
        return c != null ? TransferEncoding.parseTransferEncoding(c) : null;
    }

    public Connection connection() {
        var c = get(CONNECTION);
        return c != null ? Connection.parseConnection(c) : null;
    }

    public ScxHttpHeadersWritable transferEncoding(TransferEncoding transferEncoding) {
        set(TRANSFER_ENCODING, transferEncoding.encode());
        return this;
    }

    public ScxHttpHeadersWritable transferEncoding(ScxEncodingType... scxEncodingType) {
        return transferEncoding(new TransferEncoding(scxEncodingType));
    }

    public ScxHttpHeadersWritable connection(Connection connection) {
        set(CONNECTION, connection.encode());
        return this;
    }

    public ScxHttpHeadersWritable connection(ConnectionType... connectionType) {
        return connection(new Connection(connectionType));
    }

}
