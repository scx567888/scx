package cool.scx.app.ext.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

public final class EmptyUpdateColumnException extends BadRequestException {

    public EmptyUpdateColumnException() {
        super(Result.fail("empty-update-column").toJson(""));
    }

}
