package cool.scx.http.status;

public sealed interface ScxHttpStatus permits HttpStatus, ScxHttpStatusImpl {

    static ScxHttpStatus of(int code) {
        // 优先使用 HttpStatus
        var s = HttpStatus.find(code);
        return s != null ? s : new ScxHttpStatusImpl(code);
    }

    static ScxHttpStatus of(int code, String description) {
        // 优先使用 HttpStatus
        var s = HttpStatus.find(code);
        // 描述相同时也 直接使用 HttpStatus
        return s != null && (description == null || s.description().equals(description)) ? s : new ScxHttpStatusImpl(code, description);
    }

    int code();

    String description();

    default boolean equals(ScxHttpStatus other) {
        return this.code() == other.code();
    }

}
