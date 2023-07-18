package cool.scx.data.query;

import cool.scx.util.ArrayUtils;
import cool.scx.util.StringUtils;

public record WhereClause(String whereClause, Object... params) {

    /**
     * 拼接
     *
     * @param other a
     * @return a
     */
    public WhereClause concat(WhereClause other) {
        return new WhereClause(StringUtils.concat(whereClause, other.whereClause), ArrayUtils.concat(params, other.params));
    }

    public boolean isEmpty() {
        return (whereClause == null || whereClause.isEmpty()) && (params == null || params.length == 0);
    }

}
