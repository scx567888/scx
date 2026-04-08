package cool.scx.object.parser;

public class NodeParseException extends RuntimeException {

    public NodeParseException(Throwable cause) {
        super(cause);
    }

    public NodeParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
