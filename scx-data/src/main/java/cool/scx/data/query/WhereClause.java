package cool.scx.data.query;

import cool.scx.common.util.ArrayUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.data.Query;

public final class WhereClause extends QueryLike<WhereClause>{

    private final String clause;
    private final Object[] params;

    public WhereClause(String clause, Object... params) {
        this.clause = clause;
        this.params = params;
    }

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
        return (clause == null || clause.isEmpty()) && (params == null || params.length == 0);
    }

    public String clause() {
        return clause;
    }

    public Object[] params() {
        return params;
    }

    @Override
    protected Query toQuery() {
        return new QueryImpl().where(this);
    }

}
