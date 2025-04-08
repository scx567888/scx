package cool.scx.http.headers;

/// ScxHttpHeaderNameImpl
///
/// @author scx567888
/// @version 0.0.1
record ScxHttpHeaderNameImpl(String value) implements ScxHttpHeaderName {

    @Override
    public String toString() {
        return value;
    }

}
