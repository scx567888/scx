package cool.scx.mvc.exception;

import static cool.scx.enumeration.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

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
        super(UNSUPPORTED_MEDIA_TYPE.statusCode(), UNSUPPORTED_MEDIA_TYPE.reasonPhrase());
    }

    /**
     * a
     *
     * @param info a
     */
    public UnsupportedMediaTypeException(String info) {
        super(UNSUPPORTED_MEDIA_TYPE.statusCode(), UNSUPPORTED_MEDIA_TYPE.reasonPhrase(), info);
    }

    /**
     * a
     *
     * @param throwable a
     */
    public UnsupportedMediaTypeException(Throwable throwable) {
        super(UNSUPPORTED_MEDIA_TYPE.statusCode(), UNSUPPORTED_MEDIA_TYPE.reasonPhrase(), throwable);
    }

    /**
     * a
     *
     * @param info      a
     * @param throwable a
     */
    public UnsupportedMediaTypeException(String info, Throwable throwable) {
        super(UNSUPPORTED_MEDIA_TYPE.statusCode(), UNSUPPORTED_MEDIA_TYPE.reasonPhrase(), info, throwable);
    }

}
