package cool.scx.mvc.exception;


import cool.scx.mvc.ScxHttpException;
import cool.scx.mvc.ScxHttpResponseStatus;

/**
 * 登录了但是没权限
 *
 * @author scx567888
 * @version 1.1.19
 */
public class NoPermException extends ScxHttpException {

    /**
     * a
     */
    public NoPermException() {
        super(ScxHttpResponseStatus.NO_PERM.statusCode(), ScxHttpResponseStatus.NO_PERM.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public NoPermException(String info) {
        super(ScxHttpResponseStatus.NO_PERM.statusCode(), ScxHttpResponseStatus.NO_PERM.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public NoPermException(Throwable throwable) {
        super(ScxHttpResponseStatus.NO_PERM.statusCode(), ScxHttpResponseStatus.NO_PERM.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public NoPermException(String info, Throwable throwable) {
        super(ScxHttpResponseStatus.NO_PERM.statusCode(), ScxHttpResponseStatus.NO_PERM.reasonPhrase(), info, throwable);
    }

}
