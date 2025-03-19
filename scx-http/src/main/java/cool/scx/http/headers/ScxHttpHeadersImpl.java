package cool.scx.http.headers;

import cool.scx.http.parameters.ParametersImpl;

/// ScxHttpHeadersImpl
///
/// @author scx567888
/// @version 0.0.1
public class ScxHttpHeadersImpl extends ParametersImpl<ScxHttpHeaderName, String> implements ScxHttpHeadersWritable {

    public ScxHttpHeadersImpl(ScxHttpHeaders h) {
        super(h);
    }

    public ScxHttpHeadersImpl() {

    }

}
