package cool.scx.data.mysql_x.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.data.query.WhereOption;
import cool.scx.data.query.WhereType;
import cool.scx.data.query.exception.ValidParamListIsEmptyException;
import cool.scx.data.query.parser.WhereClauseAndWhereParams;
import cool.scx.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static cool.scx.util.ObjectUtils.*;

/**
 * WhereTypeHandler
 *
 * @author scx567888
 * @version 0.0.1
 */
interface WhereTypeHandler {

    WhereTypeHandler IS_NULL_HANDLER = (name, whereType, value1, value2, info) -> {
        var whereParams = new Object[]{};
        var whereClause = name + " " + whereType.keyWord();
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    };

    /**
     * 相同的实现 我们使用相同的方法
     */
    WhereTypeHandler IS_NOT_NULL_HANDLER = IS_NULL_HANDLER;

    WhereTypeHandler EQUAL_HANDLER = (name, whereType, value1, value2, info) -> {
        Object[] whereParams = new Object[]{value1};
        String v1 = "?";
        var whereClause = name + " " + whereType.keyWord() + " " + v1;
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    };

    WhereTypeHandler NOT_EQUAL_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler LESS_THAN_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler LESS_THAN_OR_EQUAL_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler GREATER_THAN_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler GREATER_THAN_OR_EQUAL_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler LIKE_REGEX_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler NOT_LIKE_REGEX_HANDLER = EQUAL_HANDLER;

    WhereTypeHandler LIKE_HANDLER = (name, whereType, value1, value2, info) -> {
        String v1;
        Object[] whereParams;
        v1 = "?";
        whereParams = new Object[]{value1};
        var whereClause = name + " " + whereType.keyWord() + " CONCAT('%'," + v1 + ",'%')";
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    };

    WhereTypeHandler NOT_LIKE_HANDLER = LIKE_HANDLER;

    WhereTypeHandler IN_HANDLER = (name, whereType, value1, value2, info) -> {
        String v1;
        Object[] whereParams;
        //移除空值并去重
        whereParams = Arrays.stream(toObjectArray(value1)).filter(Objects::nonNull).distinct().toArray();
        //长度为空是抛异常
        if (whereParams.length == 0) {
            throw new ValidParamListIsEmptyException(whereType);
        }
        v1 = "(" + StringUtils.repeat("?", ", ", whereParams.length) + ")";
        var whereClause = name + " " + whereType.keyWord() + " " + v1;
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    };

    WhereTypeHandler NOT_IN_HANDLER = IN_HANDLER;

    WhereTypeHandler BETWEEN_HANDLER = (name, whereType, value1, value2, info) -> {
        String v1;
        String v2;
        var whereParams = new ArrayList<>();
        v1 = "?";
        whereParams.add(value1);
        v2 = "?";
        whereParams.add(value2);
        var whereClause = name + " " + whereType.keyWord() + " " + v1 + " AND " + v2;
        return new WhereClauseAndWhereParams(whereClause, whereParams.toArray());
    };

    WhereTypeHandler NOT_BETWEEN_HANDLER = BETWEEN_HANDLER;

    WhereTypeHandler JSON_CONTAINS_HANDLER = (name, whereType, value1, value2, info) -> {
        String v1;
        Object[] whereParams;

        v1 = "?";
        if (info.useOriginalValue()) {
            whereParams = new Object[]{value1};
        } else {
            try {
                whereParams = new Object[]{toJson(value1, Option.IGNORE_JSON_IGNORE, Option.IGNORE_NULL_VALUE)};
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询参数不合法(无法正确转换为 JSON) !!! 字段名 : " + name, e);
            }
        }

        var whereClause = whereType.keyWord() + "(" + name;
//        if (StringUtils.notBlank(c.fieldPath())) {
//            whereClause = whereClause + ", " + v1 + ", '$" + c.fieldPath() + "')";
//        } else {
        whereClause = whereClause + ", " + v1 + ")";
//        }
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    };


    WhereClauseAndWhereParams getWhereClauseAndWhereParams(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info);

}
