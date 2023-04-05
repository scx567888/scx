package cool.scx.data.jdbc.parser;

import cool.scx.data.AnnotationConfigTable;
import cool.scx.data.query.WhereBody;
import cool.scx.data.query.WhereType;
import cool.scx.data.query.parser.WhereClauseAndWhereParams;
import cool.scx.data.query.parser.WhereParser;

import static cool.scx.data.jdbc.parser.WhereTypeHandler.*;

public class JDBCDaoWhereParser extends WhereParser {

    private final AnnotationConfigTable tableInfo;

    public JDBCDaoWhereParser(AnnotationConfigTable tableInfo) {
        this.tableInfo = tableInfo;
    }

    private static WhereTypeHandler findWhereTypeHandler(WhereType whereType) {
        return switch (whereType) {
            case IS_NULL -> IS_NULL_HANDLER;
            case IS_NOT_NULL -> IS_NOT_NULL_HANDLER;
            case EQUAL -> EQUAL_HANDLER;
            case NOT_EQUAL -> NOT_EQUAL_HANDLER;
            case LESS_THAN -> LESS_THAN_HANDLER;
            case LESS_THAN_OR_EQUAL -> LESS_THAN_OR_EQUAL_HANDLER;
            case GREATER_THAN -> GREATER_THAN_HANDLER;
            case GREATER_THAN_OR_EQUAL -> GREATER_THAN_OR_EQUAL_HANDLER;
            case LIKE -> LIKE_HANDLER;
            case NOT_LIKE -> NOT_LIKE_HANDLER;
            case LIKE_REGEX -> LIKE_REGEX_HANDLER;
            case NOT_LIKE_REGEX -> NOT_LIKE_REGEX_HANDLER;
            case IN -> IN_HANDLER;
            case NOT_IN -> NOT_IN_HANDLER;
            case BETWEEN -> BETWEEN_HANDLER;
            case NOT_BETWEEN -> NOT_BETWEEN_HANDLER;
            case JSON_CONTAINS -> JSON_CONTAINS_HANDLER;
        };
    }

    @Override
    public WhereClauseAndWhereParams parseWhereBody(WhereBody body) {
        return findWhereTypeHandler(body.whereType()).getWhereParamsAndWhereClause(tableInfo, body.name(), body.whereType(), body.value1(), body.value2(), body.info());
    }

}
