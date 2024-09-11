package cool.scx.ext.crud.exception;

import cool.scx.data.field_filter.FilterMode;
import cool.scx.web.exception.BadRequestException;
import cool.scx.web.vo.Result;

public final class EmptySelectColumnException extends BadRequestException {

    public EmptySelectColumnException(FilterMode filterMode, String[] fieldNames) {
        super(Result.fail("empty-select-column").put("filter-mode", filterMode).put("field-names", fieldNames).toJson(""));
    }

}
