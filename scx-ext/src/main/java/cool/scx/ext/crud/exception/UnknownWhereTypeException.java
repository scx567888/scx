package cool.scx.ext.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

public final class UnknownWhereTypeException extends BadRequestException {

    public UnknownWhereTypeException(String fieldName, String strWhereType) {
        super(Result.fail("unknown-where-type").put("field-name", fieldName).put("where-type", strWhereType).toJson(""));
    }

}
