package cool.scx.object.serializer;

public class NodeSerializeException extends RuntimeException {

    public NodeSerializeException(Throwable cause) {
        super(cause);
    }

    public NodeSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NodeSerializeException(String message) {
        super(message);
    }

}
