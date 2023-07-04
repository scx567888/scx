package cool.scx.data.jdbc.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.jdbc.dialect.Dialect;
import cool.scx.data.jdbc.sql.SQL;
import cool.scx.data.query.WhereBody;
import cool.scx.data.query.WhereOption;
import cool.scx.data.query.WhereType;
import cool.scx.data.query.exception.ValidParamListIsEmptyException;
import cool.scx.data.query.parser.WhereClauseAndWhereParams;
import cool.scx.data.query.parser.WhereParser;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static cool.scx.data.jdbc.parser.ColumnNameParser.parseColumnName;
import static cool.scx.data.jdbc.parser.ColumnNameParser.splitIntoColumnNameAndFieldPath;
import static cool.scx.util.ObjectUtils.toJson;
import static cool.scx.util.ObjectUtils.toObjectArray;

public class JDBCDaoWhereParser extends WhereParser {

    private final AnnotationConfigTable tableInfo;
    private final Dialect dialect;

    public JDBCDaoWhereParser(AnnotationConfigTable tableInfo, Dialect dialect) {
        this.tableInfo = tableInfo;
        this.dialect = dialect;
    }

    public WhereClauseAndWhereParams parseIsNull(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = new Object[]{};
        var whereClause = columnName + " " + dialect.getWhereKeyWord(whereType);
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    public WhereClauseAndWhereParams parseEqual(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        Object[] whereParams;
        //针对 参数类型是 AbstractPlaceholderSQL 的情况进行特殊处理 下同
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            v1 = "?";
            whereParams = new Object[]{value1};
        }
        var whereClause = columnName + " " + dialect.getWhereKeyWord(whereType) + " " + v1;
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    public WhereClauseAndWhereParams parseLike(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        Object[] whereParams;
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            v1 = "?";
            whereParams = new Object[]{value1};
        }
        var whereClause = columnName + " " + dialect.getWhereKeyWord(whereType) + " CONCAT('%'," + v1 + ",'%')";
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    public WhereClauseAndWhereParams parseIn(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        Object[] whereParams;
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            //移除空值并去重
            whereParams = Arrays.stream(toObjectArray(value1)).filter(Objects::nonNull).distinct().toArray();
            //长度为空是抛异常
            if (whereParams.length == 0) {
                throw new ValidParamListIsEmptyException(whereType);
            }
            v1 = "(" + StringUtils.repeat("?", ", ", whereParams.length) + ")";
        }
        var whereClause = columnName + " " + dialect.getWhereKeyWord(whereType) + " " + v1;
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    public WhereClauseAndWhereParams parseBetween(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        String v2;
        var whereParams = new ArrayList<>();
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            Collections.addAll(whereParams, a.params());
        } else {
            v1 = "?";
            whereParams.add(value1);
        }
        if (value2 instanceof SQL a) {
            v2 = "(" + a.sql() + ")";
            Collections.addAll(whereParams, a.params());
        } else {
            v2 = "?";
            whereParams.add(value2);
        }
        var whereClause = columnName + " " + dialect.getWhereKeyWord(whereType) + " " + v1 + " AND " + v2;
        return new WhereClauseAndWhereParams(whereClause, whereParams.toArray());
    }

    public WhereClauseAndWhereParams parseJsonContains(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        var c = splitIntoColumnNameAndFieldPath(name);
        var columnName = info.useOriginalName() ? c.columnName() : tableInfo.getColumn(c.columnName()).name();
        if (StringUtils.isBlank(c.columnName())) {
            throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询名称不合法 !!! 字段名 : " + name);
        }
        String v1;
        Object[] whereParams;
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            v1 = "?";
            if (info.useOriginalValue()) {
                whereParams = new Object[]{value1};
            } else {
                try {
                    whereParams = new Object[]{toJson(value1, ObjectUtils.Option.IGNORE_JSON_IGNORE, ObjectUtils.Option.IGNORE_NULL_VALUE)};
                } catch (JsonProcessingException e) {
                    throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询参数不合法(无法正确转换为 JSON) !!! 字段名 : " + name, e);
                }
            }
        }
        var whereClause = dialect.getWhereKeyWord(whereType) + "(" + columnName;
        if (StringUtils.notBlank(c.fieldPath())) {
            whereClause = whereClause + ", " + v1 + ", '$" + c.fieldPath() + "')";
        } else {
            whereClause = whereClause + ", " + v1 + ")";
        }
        return new WhereClauseAndWhereParams(whereClause, whereParams);
    }

    @Override
    public WhereClauseAndWhereParams parse(Object obj) {
        if (obj instanceof SQL sql) {
            return parseSQL(sql);
        }
        return super.parse(obj);
    }

    public final WhereClauseAndWhereParams parseSQL(SQL sql) {
        return new WhereClauseAndWhereParams("(" + sql.sql() + ")", sql.params());
    }

    @Override
    public WhereClauseAndWhereParams parseWhereBody(WhereBody body) {
        var name = body.name();
        var whereType = body.whereType();
        var value1 = body.value1();
        var value2 = body.value2();
        var info = body.info();
        return switch (whereType) {
            case IS_NULL, IS_NOT_NULL -> parseIsNull(name, whereType, value1, value2, info);
            case EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LIKE_REGEX, NOT_LIKE_REGEX ->
                    parseEqual(name, whereType, value1, value2, info);
            case LIKE, NOT_LIKE -> parseLike(name, whereType, value1, value2, info);
            case IN, NOT_IN -> parseIn(name, whereType, value1, value2, info);
            case BETWEEN, NOT_BETWEEN -> parseBetween(name, whereType, value1, value2, info);
            case JSON_CONTAINS -> parseJsonContains(name, whereType, value1, value2, info);
        };
    }

}
