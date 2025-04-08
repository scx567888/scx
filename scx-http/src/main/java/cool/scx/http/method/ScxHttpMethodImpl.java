package cool.scx.http.method;

/// ScxHttpMethodImpl
///
/// @author scx567888
/// @version 0.0.1
record ScxHttpMethodImpl(String value) implements ScxHttpMethod {

    @Override
    public String toString() {
        return value;
    }

}
