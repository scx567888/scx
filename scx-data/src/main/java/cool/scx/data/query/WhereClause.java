package cool.scx.data.query;

import cool.scx.data.Query0;
import cool.scx.util.ArrayUtils;
import cool.scx.util.StringUtils;

import static cool.scx.data.query.Where.where;

public record WhereClause(String whereClause, Object... params) implements Query0 {

    public static WhereClause whereClause(String whereClause, Object... params) {
        return new WhereClause(whereClause, params);
    }

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

    @Override
    public Where getWhere() {
        return where(this);
    }

}
