package cool.scx.jdbc.sql;

/**
 * 无参数的 cool.scx.sql
 *
 * @author scx567888
 * @version 0.0.7
 */
final class NormalSQL implements SQL {

    /**
     * a
     */
    private final String normalSQL;

    public NormalSQL(String normalSQL) {
        this.normalSQL = normalSQL;
    }

    @Override
    public String sql() {
        return normalSQL;
    }

}
