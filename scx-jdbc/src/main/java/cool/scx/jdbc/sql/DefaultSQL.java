package cool.scx.jdbc.sql;

import java.util.ArrayList;
import java.util.List;

/// 默认 问号形式 (?) 占位的 SQL
///
/// @author scx567888
/// @version 0.0.1
final class DefaultSQL implements SQL {

    private final String sql;
    private final Object[] params;
    private final List<Object[]> batchParams;
    private final boolean isBatch;

    DefaultSQL(String sql, Object[] params) {
        this.sql = sql;
        this.params = params;
        this.batchParams = new ArrayList<>();
        this.isBatch = false;
    }

    DefaultSQL(String sql, List<Object[]> batchParams) {
        this.sql = sql;
        this.params = new Object[]{};
        this.batchParams = batchParams;
        this.isBatch = true;
    }

    @Override
    public String sql() {
        return sql;
    }

    @Override
    public Object[] params() {
        return params;
    }

    @Override
    public List<Object[]> batchParams() {
        return batchParams;
    }

    @Override
    public boolean isBatch() {
        return isBatch;
    }

}
