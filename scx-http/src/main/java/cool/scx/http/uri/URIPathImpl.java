package cool.scx.http.uri;

/**
 * URIPathImpl
 */
class URIPathImpl implements URIPathWritable {

    private String value;

    @Override
    public URIPathWritable value(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String value() {
        return value;
    }

}
