package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * MapListHandler
 *
 * @author scx567888
 * @version 0.0.1
 */
record MapListHandler(Supplier<Map<String, Object>> mapSupplier) implements ResultHandler<List<Map<String, Object>>> {

    static final MapListHandler INSTANCE = new MapListHandler();

    public MapListHandler() {
        this(HashMap::new);
    }

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public List<Map<String, Object>> apply(ResultSet rs, Dialect dialect) throws SQLException {
        var list = new ArrayList<Map<String, Object>>();
        var rsm = rs.getMetaData();
        var count = rsm.getColumnCount();
        while (rs.next()) {
            var map = mapSupplier.get();
            for (int i = 1; i <= count; i = i + 1) {
                map.put(rsm.getColumnLabel(i), rs.getObject(i));
            }
            list.add(map);
        }
        return list;
    }

}
