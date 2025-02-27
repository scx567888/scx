package cool.scx.http;

/// ScxHttpHeadersImpl
///
/// @author scx567888
/// @version 0.0.1
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
