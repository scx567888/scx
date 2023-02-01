package cool.scx.mvc.exception;


import cool.scx.mvc.ScxHttpException;
import cool.scx.mvc.ScxHttpResponseStatus;

/**
 * 415 参数异常
 *
 * @author scx567888
 * @version 1.1.14
 */
public class UnsupportedMediaTypeException extends ScxHttpException {

    /**
     * a
     */
    public UnsupportedMediaTypeException() {
        super(ScxHttpResponseStatus.UNSUPPORTED_MEDIA_TYPE.statusCode(), ScxHttpResponseStatus.UNSUPPORTED_MEDIA_TYPE.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public UnsupportedMediaTypeException(String info) {
        super(ScxHttpResponseStatus.UNSUPPORTED_MEDIA_TYPE.statusCode(), ScxHttpResponseStatus.UNSUPPORTED_MEDIA_TYPE.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public UnsupportedMediaTypeException(Throwable throwable) {
        super(ScxHttpResponseStatus.UNSUPPORTED_MEDIA_TYPE.statusCode(), ScxHttpResponseStatus.UNSUPPORTED_MEDIA_TYPE.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public UnsupportedMediaTypeException(String info, Throwable throwable) {
        super(ScxHttpResponseStatus.UNSUPPORTED_MEDIA_TYPE.statusCode(), ScxHttpResponseStatus.UNSUPPORTED_MEDIA_TYPE.reasonPhrase(), info, throwable);
    }

}
