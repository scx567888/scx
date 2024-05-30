package cool.scx.ext.crud.exception;

import cool.scx.web.exception.BadRequestException;
import cool.scx.web.vo.Result;

public final class EmptyUpdateColumnException extends BadRequestException {

    public EmptyUpdateColumnException() {
        super(Result.fail("empty-update-column").toJson(""));
    }

}
