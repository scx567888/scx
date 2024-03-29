package cool.scx.mvc.exception;

import static cool.scx.common.standard.HttpStatusCode.INTERNAL_SERVER_ERROR;

/**
 * 服务器内部异常
 *
 * @author scx567888
 * @version 1.11.8
 */
public class InternalServerErrorException extends ScxHttpException {

    /**
     * a
     */
    public InternalServerErrorException() {
        super(INTERNAL_SERVER_ERROR.statusCode(), INTERNAL_SERVER_ERROR.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public InternalServerErrorException(String info) {
        super(INTERNAL_SERVER_ERROR.statusCode(), INTERNAL_SERVER_ERROR.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public InternalServerErrorException(Throwable throwable) {
        super(INTERNAL_SERVER_ERROR.statusCode(), INTERNAL_SERVER_ERROR.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public InternalServerErrorException(String info, Throwable throwable) {
        super(INTERNAL_SERVER_ERROR.statusCode(), INTERNAL_SERVER_ERROR.reasonPhrase(), info, throwable);
    }

}
