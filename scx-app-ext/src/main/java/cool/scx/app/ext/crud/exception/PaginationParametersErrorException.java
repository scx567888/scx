package cool.scx.app.ext.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

public final class PaginationParametersErrorException extends BadRequestException {

    public PaginationParametersErrorException(Long currentPage, Long pageSize) {
        super(Result.fail("pagination-parameters-error").put("info", "currentPage 和 pageSize 均不能小于 0").put("currentPage", currentPage).put("pageSize", pageSize).toJson(""));
    }

}
