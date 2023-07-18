package cool.scx.data.query;

import cool.scx.util.ArrayUtils;
import cool.scx.util.StringUtils;

public record WhereClause(String clause, Object... params) {

    /**
     * 拼接
     *
     * @param other a
     * @return a
     */
    public WhereClause concat(WhereClause other) {
        return new WhereClause(StringUtils.concat(clause, other.clause), ArrayUtils.concat(params, other.params));
    }

    public boolean isEmpty() {
        return (clause == null|| clause.isEmpty()) && (params == null || params.length == 0);
    }

}
