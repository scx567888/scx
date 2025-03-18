package cool.scx.http.status;

public interface ScxHttpStatus {

    static ScxHttpStatus of(int code) {
        // 优先使用 HttpStatus
        var s = HttpStatus.find(code);
        return s != null ? s : new ScxHttpStatusImpl(code);
    }

    static ScxHttpStatus of(int code, String description) {
        return new ScxHttpStatusImpl(code, description);
    }

    int code();

    String description();

    default boolean equals(ScxHttpStatus other) {
        return this.code() == other.code();
    }

}
