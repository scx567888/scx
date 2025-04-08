package cool.scx.http.status;

/// ScxHttpStatusImpl
///
/// @author scx567888
/// @version 0.0.1
record ScxHttpStatusImpl(int code) implements ScxHttpStatus {

    @Override
    public String toString() {
        return code + "";
    }

}
