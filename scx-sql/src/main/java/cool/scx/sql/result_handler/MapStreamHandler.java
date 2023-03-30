package cool.scx.sql.result_handler;

import cool.scx.sql.ResultStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * a
 *
 * @param mapSupplier a
 */
record MapStreamHandler(
        Supplier<Map<String, Object>> mapSupplier) implements ResultHandler<ResultStream<Map<String, Object>>> {

    static final MapStreamHandler INSTANCE = new MapStreamHandler();

    public MapStreamHandler() {
        this(HashMap::new);
    }

    @Override
    public ResultStream<Map<String, Object>> apply(ResultSet rs) throws SQLException {
        var rsm = rs.getMetaData();
        var count = rsm.getColumnCount();
        return new ResultStream<>(c -> {
            var map = mapSupplier.get();
            for (int i = 1; i <= count; i = i + 1) {
                map.put(rsm.getColumnLabel(i), rs.getObject(i));
            }
            return map;
        }, rs);
    }

}
