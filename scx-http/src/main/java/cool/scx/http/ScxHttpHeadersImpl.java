package cool.scx.http;

/**
 * ScxHttpHeadersImpl
 */
class ScxHttpHeadersImpl extends ParametersImpl<ScxHttpHeaderName, String> implements ScxHttpHeadersWritable {

    @Override
    public String toString() {
        return encode();
    }

}
