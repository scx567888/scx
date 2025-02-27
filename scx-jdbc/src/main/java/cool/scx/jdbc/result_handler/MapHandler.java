package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/// MapHandler
///
/// @author scx567888
/// @version 0.0.1
record MapHandler(Supplier<Map<String, Object>> mapSupplier) implements ResultHandler<Map<String, Object>> {

    static final MapHandler INSTANCE = new MapHandler();

    public MapHandler() {
        this(HashMap::new);
    }

    @Override
    public Map<String, Object> apply(ResultSet rs, Dialect dialect) throws SQLException {
        var rsm = rs.getMetaData();
        var count = rsm.getColumnCount();
        if (rs.next()) {
            var map = this.mapSupplier.get();
            for (int i = 1; i <= count; i = i + 1) {
                map.put(rsm.getColumnLabel(i), rs.getObject(i));
            }
            return map;
        } else {
            return null;
        }
    }

}
