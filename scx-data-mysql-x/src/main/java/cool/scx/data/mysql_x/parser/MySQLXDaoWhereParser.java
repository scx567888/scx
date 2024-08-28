package cool.scx.data.mysql_x.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.data.query.Where;
import cool.scx.data.query.WhereClause;
import cool.scx.data.query.exception.ValidParamListIsEmptyException;
import cool.scx.data.query.parser.WhereParser;

import java.util.Arrays;
import java.util.Objects;

import static cool.scx.common.util.ArrayUtils.toObjectArray;
import static cool.scx.common.util.ObjectUtils.toJson;

public class MySQLXDaoWhereParser extends WhereParser {

    public static final MySQLXDaoWhereParser WHERE_PARSER = new MySQLXDaoWhereParser();

    @Override
    public WhereClause parseIsNull(Where w) {
        var whereParams = new Object[]{};
        var whereClause = w.name() + " " + getWhereKeyWord(w.whereType());
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseEqual(Where w) {
        var whereParams = new Object[]{w.value1()};
        var whereClause = w.name() + " " + getWhereKeyWord(w.whereType()) + " ?";
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseLike(Where w) {
        var whereParams = new Object[]{w.value1()};
        var whereClause = w.name() + " " + getWhereKeyWord(w.whereType()) + " '%?%'";
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseIn(Where w) {
        //移除空值并去重
        var whereParams = Arrays.stream(toObjectArray(w.value1())).filter(Objects::nonNull).distinct().toArray();
        //长度为空是抛异常
        if (whereParams.length == 0) {
            throw new ValidParamListIsEmptyException(w.whereType());
        }
        var v1 = "(" + StringUtils.repeat("?", ", ", whereParams.length) + ")";
        var whereClause = w.name() + " " + getWhereKeyWord(w.whereType()) + " " + v1;
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseBetween(Where w) {
        var whereParams = new Object[]{w.value1(), w.value2()};
        var whereClause = w.name() + " " + getWhereKeyWord(w.whereType()) + " ? AND ?";
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseJsonContains(Where w) {
        Object[] whereParams;
        String v1 = "?";
        if (w.info().useOriginalValue()) {
            whereParams = new Object[]{w.value1()};
        } else {
            try {
                whereParams = new Object[]{toJson(w.value1(), new ObjectUtils.Options().setIgnoreJsonIgnore(true).setIgnoreNullValue(true))};
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询参数不合法(无法正确转换为 JSON) !!! 字段名 : " + w.name(), e);
            }
        }

        var whereClause = getWhereKeyWord(w.whereType()) + "(" + w.name();
//        if (StringUtils.notBlank(c.fieldPath())) {
//            whereClause = whereClause + ", " + v1 + ", '$" + c.fieldPath() + "')";
//        } else {
        whereClause = whereClause + ", " + v1 + ")";
//        }
        return new WhereClause(whereClause, whereParams);
    }

}
