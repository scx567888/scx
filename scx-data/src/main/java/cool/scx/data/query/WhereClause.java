package cool.scx.data.query;

import cool.scx.common.util.ArrayUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.data.Query;

public final class WhereClause extends QueryLike<WhereClause> {

    private final String whereClause;
    private final Object[] params;

    public WhereClause(String whereClause, Object... params) {
        this.whereClause = whereClause;
        this.params = params;
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

    public String whereClause() {
        return whereClause;
    }

    public Object[] params() {
        return params;
    }

    @Override
    protected Query toQuery() {
        return new QueryImpl().where(this);
    }

}
