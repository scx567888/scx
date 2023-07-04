package cool.scx.data.mysql_x.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.data.query.WhereOption;
import cool.scx.data.query.WhereType;
import cool.scx.data.query.exception.ValidParamListIsEmptyException;
import cool.scx.data.query.parser.WhereClauseAndWhereParams;
import cool.scx.data.query.parser.WhereParser;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

import static cool.scx.util.ObjectUtils.toJson;
import static cool.scx.util.ObjectUtils.toObjectArray;

public class MySQLXDaoWhereParser extends WhereParser {

    public static final MySQLXDaoWhereParser WHERE_PARSER = new MySQLXDaoWhereParser();

    @Override
    public WhereClauseAndWhereParams parseIsNull(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var whereParams = new Object[]{};
        var whereClause = name + " " + getWhereKeyWord(whereType);
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    @Override
    public WhereClauseAndWhereParams parseEqual(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var whereParams = new Object[]{value1};
        var whereClause = name + " " + getWhereKeyWord(whereType) + " ?";
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    @Override
    public WhereClauseAndWhereParams parseLike(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var whereParams = new Object[]{value1};
        var whereClause = name + " " + getWhereKeyWord(whereType) + " '%?%'";
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    @Override
    public WhereClauseAndWhereParams parseIn(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        //移除空值并去重
        var whereParams = Arrays.stream(toObjectArray(value1)).filter(Objects::nonNull).distinct().toArray();
        //长度为空是抛异常
        if (whereParams.length == 0) {
            throw new ValidParamListIsEmptyException(whereType);
        }
        var v1 = "(" + StringUtils.repeat("?", ", ", whereParams.length) + ")";
        var whereClause = name + " " + getWhereKeyWord(whereType) + " " + v1;
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    @Override
    public WhereClauseAndWhereParams parseBetween(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var whereParams = new Object[]{value1, value2};
        var whereClause = name + " " + getWhereKeyWord(whereType) + " ? AND ?";
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    @Override
    public WhereClauseAndWhereParams parseJsonContains(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        Object[] whereParams;
        String v1 = "?";
        if (info.useOriginalValue()) {
            whereParams = new Object[]{value1};
        } else {
            try {
                whereParams = new Object[]{toJson(value1, ObjectUtils.Option.IGNORE_JSON_IGNORE, ObjectUtils.Option.IGNORE_NULL_VALUE)};
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询参数不合法(无法正确转换为 JSON) !!! 字段名 : " + name, e);
            }
        }

        var whereClause = getWhereKeyWord(whereType) + "(" + name;
//        if (StringUtils.notBlank(c.fieldPath())) {
//            whereClause = whereClause + ", " + v1 + ", '$" + c.fieldPath() + "')";
//        } else {
        whereClause = whereClause + ", " + v1 + ")";
//        }
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

}
