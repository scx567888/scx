package cool.scx.exception.impl;

import cool.scx.ScxConstant;
import cool.scx.exception.ScxHttpException;

public class InternalServerErrorException extends ScxHttpException {

    public InternalServerErrorException() {
        super(500, ScxConstant.HTTP_INTERNAL_SERVER_ERROR_TITLE, "");
    }

    public InternalServerErrorException(String info) {
        super(500, ScxConstant.HTTP_INTERNAL_SERVER_ERROR_TITLE, info);
    }

}
