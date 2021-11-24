package cool.scx.exception;

public record ScxHttpExceptionViewWrapper(int httpCode, String title, String info) {

}
