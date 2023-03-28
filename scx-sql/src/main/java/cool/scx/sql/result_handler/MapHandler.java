package cool.scx.sql.result_handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
final class MapHandler implements ResultHandler<Map<String, Object>> {

    static final MapHandler INSTANCE = new MapHandler();

    public final Supplier<Map<String, Object>> mapSupplier;

    public MapHandler() {
        this(HashMap::new);
    }

    public MapHandler(Supplier<Map<String, Object>> mapSupplier) {
        this.mapSupplier = mapSupplier;
    }

    @Override
    public Map<String, Object> apply(ResultSet rs) throws SQLException {
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
