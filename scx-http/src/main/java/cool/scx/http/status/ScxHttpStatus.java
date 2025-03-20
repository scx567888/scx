package cool.scx.http.status;

public sealed interface ScxHttpStatus permits HttpStatus, ScxHttpStatusImpl {

    static ScxHttpStatus of(int code) {
        // 优先使用 HttpStatus
        var s = HttpStatus.find(code);
        return s != null ? s : new ScxHttpStatusImpl(code);
    }

    int code();

}
