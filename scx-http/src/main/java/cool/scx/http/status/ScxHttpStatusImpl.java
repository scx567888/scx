package cool.scx.http.status;

public record ScxHttpStatusImpl(int code, String description) implements ScxHttpStatus {

    public ScxHttpStatusImpl(int code) {
        this(code, code + "");
    }

}
