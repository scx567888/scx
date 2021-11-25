package cool.scx.exception;

public final class ScxWrappedRuntimeException extends RuntimeException {

    public ScxWrappedRuntimeException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

}
