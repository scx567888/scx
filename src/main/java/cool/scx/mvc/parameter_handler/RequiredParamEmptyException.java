package cool.scx.mvc.parameter_handler;

public class RequiredParamEmptyException extends Exception {

    public RequiredParamEmptyException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}