package cool.scx.dao.query.parser;

import cool.scx.util.ArrayUtils;
import cool.scx.util.StringUtils;

public record WhereClauseAndWhereParams(String whereClause, Object[] whereParams) {

    /**
     * 拼接
     *
     * @param other a
     * @return a
     */
    public WhereClauseAndWhereParams concat(WhereClauseAndWhereParams other) {
        return new WhereClauseAndWhereParams(StringUtils.concat(whereClause, other.whereClause), ArrayUtils.concat(whereParams, other.whereParams));
    }

}
