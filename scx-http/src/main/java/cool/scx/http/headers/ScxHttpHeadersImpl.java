package cool.scx.http.headers;

import cool.scx.http.parameters.ParametersImpl;

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
    public ScxHttpHeadersImpl set(ScxHttpHeaderName name, String... value) {
        return (ScxHttpHeadersImpl) super.set(name, value);
    }

    @Override
    public ScxHttpHeadersImpl add(ScxHttpHeaderName name, String... value) {
        return (ScxHttpHeadersImpl) super.add(name, value);
    }

    @Override
    public ScxHttpHeadersImpl remove(ScxHttpHeaderName name) {
        return (ScxHttpHeadersImpl) super.remove(name);
    }

    @Override
    public ScxHttpHeadersImpl clear() {
        return (ScxHttpHeadersImpl) super.clear();
    }

    @Override
    public String toString() {
        return encode();
    }

}
