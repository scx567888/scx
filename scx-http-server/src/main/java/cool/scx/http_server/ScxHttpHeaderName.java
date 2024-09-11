package cool.scx.http_server;

public sealed interface ScxHttpHeaderName permits HttpFieldName, ScxHttpHeaderNameImpl {

    static ScxHttpHeaderName of(String name) {
        try {
            // 优先使用 HttpFieldName
            return HttpFieldName.of(name);
        } catch (IllegalArgumentException e) {
            return new ScxHttpHeaderNameImpl(name);
        }
    }

    String value();

}
