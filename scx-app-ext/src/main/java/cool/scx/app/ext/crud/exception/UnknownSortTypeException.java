package cool.scx.app.ext.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

public final class UnknownSortTypeException extends BadRequestException {

    public UnknownSortTypeException(String fieldName, String strSortType) {
        super(Result.fail("unknown-sort-type").put("field-name", fieldName).put("sort-type", strSortType).toJson(""));
    }

}
