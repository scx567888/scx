package cool.scx.http.status;

/// ScxHttpStatusImpl
///
/// @author scx567888
/// @version 0.0.1
record ScxHttpStatusImpl(int code, String description) implements ScxHttpStatus {

    public ScxHttpStatusImpl(int code) {
        this(code, null);
    }

}
