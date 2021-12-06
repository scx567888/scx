package cool.scx.exception;

/**
 * a
 */
public final class ScxWrappedRuntimeException extends RuntimeException {

    /**
     * a
     *
     * @param cause a
     */
    public ScxWrappedRuntimeException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

}
