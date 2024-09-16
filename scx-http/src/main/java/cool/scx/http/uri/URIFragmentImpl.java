package cool.scx.http.uri;

/**
 * URIFragmentImpl
 */
class URIFragmentImpl implements URIFragmentWritable {

    private String value;

    @Override
    public URIFragmentWritable value(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String value() {
        return value;
    }

}
