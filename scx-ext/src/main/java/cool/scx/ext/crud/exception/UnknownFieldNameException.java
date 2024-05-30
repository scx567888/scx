package cool.scx.ext.crud.exception;

import cool.scx.web.exception.BadRequestException;
import cool.scx.web.vo.Result;

public final class UnknownFieldNameException extends BadRequestException {

    public UnknownFieldNameException(String fieldName) {
        super(Result.fail("unknown-field-name").put("field-name", fieldName).toJson(""));
    }

}
