package cool.scx.data.query;

import cool.scx.common.util.ArrayUtils;
import cool.scx.common.util.StringUtils;

/// WhereClause
///
/// @author scx567888
/// @version 0.0.1
public final class WhereClause extends QueryLike<WhereClause> {

    private final String whereClause;
    private final Object[] params;

    public WhereClause(String whereClause, Object... params) {
        this.whereClause = whereClause;
        this.params = params;
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
