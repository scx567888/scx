package cool.scx.http.uri;

import static cool.scx.http.uri.URIHelper.decode;
import static cool.scx.http.uri.URIHelper.encode;

class URIPathImpl implements URIPathWritable {

    private String value;
    private String rawValue;

    @Override
    public URIPathWritable value(String value) {
        this.value = value;
        this.rawValue = encode(value);
        return this;
    }

    @Override
    public URIPathWritable rawValue(String rawValue) {
        this.rawValue = rawValue;
        this.value = decode(rawValue);
        return this;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String rawValue() {
        return rawValue;
    }

}
