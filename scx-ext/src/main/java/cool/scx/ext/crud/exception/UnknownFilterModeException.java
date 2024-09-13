package cool.scx.ext.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

public final class UnknownFilterModeException extends BadRequestException {

    public UnknownFilterModeException(String filterMode) {
        super(Result.fail("unknown-filter-mode").put("filter-mode", filterMode).toJson(""));
    }

}
