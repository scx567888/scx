package cool.scx.ext.crud.exception;

import cool.scx.data.query.WhereType;
import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;


public final class WhereBodyParametersSizeErrorException extends BadRequestException {

    public WhereBodyParametersSizeErrorException(String fieldName, WhereType whereType, int gotParametersSize) {
        super(Result.fail("where-body-parameters-size-error")
                .put("field-name", fieldName)
                .put("where-type", whereType)
                .put("need-parameters-size", -1)
                .put("got-parameters-size", gotParametersSize)
                .toJson(""));

    }

}
