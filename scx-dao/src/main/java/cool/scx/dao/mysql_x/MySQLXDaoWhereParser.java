package cool.scx.dao.mysql_x;

import cool.scx.dao.query.WhereBody;
import cool.scx.dao.query.WhereType;
import cool.scx.dao.query.parser.WhereClauseAndWhereParams;
import cool.scx.dao.query.parser.WhereParser;

public class MySQLXDaoWhereParser extends WhereParser {

    static WhereTypeHandler findWhereTypeHandler(WhereType whereType) {
        return switch (whereType) {
            case IS_NULL -> WhereTypeHandler.IS_NULL_HANDLER;
            case IS_NOT_NULL -> WhereTypeHandler.IS_NOT_NULL_HANDLER;
            case EQUAL -> WhereTypeHandler.EQUAL_HANDLER;
            case NOT_EQUAL -> WhereTypeHandler.NOT_EQUAL_HANDLER;
            case LESS_THAN -> WhereTypeHandler.LESS_THAN_HANDLER;
            case LESS_THAN_OR_EQUAL -> WhereTypeHandler.LESS_THAN_OR_EQUAL_HANDLER;
            case GREATER_THAN -> WhereTypeHandler.GREATER_THAN_HANDLER;
            case GREATER_THAN_OR_EQUAL -> WhereTypeHandler.GREATER_THAN_OR_EQUAL_HANDLER;
            case LIKE -> WhereTypeHandler.LIKE_HANDLER;
            case NOT_LIKE -> WhereTypeHandler.NOT_LIKE_HANDLER;
            case LIKE_REGEX -> WhereTypeHandler.LIKE_REGEX_HANDLER;
            case NOT_LIKE_REGEX -> WhereTypeHandler.NOT_LIKE_REGEX_HANDLER;
            case IN -> WhereTypeHandler.IN_HANDLER;
            case NOT_IN -> WhereTypeHandler.NOT_IN_HANDLER;
            case BETWEEN -> WhereTypeHandler.BETWEEN_HANDLER;
            case NOT_BETWEEN -> WhereTypeHandler.NOT_BETWEEN_HANDLER;
            case JSON_CONTAINS -> WhereTypeHandler.JSON_CONTAINS_HANDLER;
        };
    }

    @Override
    public WhereClauseAndWhereParams parseWhereBody(WhereBody body) {
        return findWhereTypeHandler(body.whereType()).getWhereClauseAndWhereParams(body.name(), body.whereType(), body.value1(), body.value2(), body.info());
    }

}
