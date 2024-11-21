package cool.scx.http;

/**
 * ScxHttpHeadersImpl
 */
class ScxHttpHeadersImpl extends ParametersImpl<ScxHttpHeaderName, String> implements ScxHttpHeadersWritable {

    public ScxHttpHeadersImpl(ScxHttpHeaders h) {
        super(h);
    }

    public ScxHttpHeadersImpl() {

    }

    @Override
    public String toString() {
        return encode();
    }

}
