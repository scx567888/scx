package cool.scx.http;

public interface ScxHttpServerErrorHandler {

    void accept(Throwable throwable, ScxHttpServerRequest request, ErrorPhase errorPhase);

    enum ErrorPhase {
        SYSTEM,
        USER
    }

}
