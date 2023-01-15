package cool.scx.sql.result_handler;

import cool.scx.sql.ResultHandler;

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
public final class MapHandler implements ResultHandler<Map<String, Object>> {

    /**
     * Constant <code>DEFAULT_MAP_LIST_HANDLER</code>
     */
    public static final MapHandler DEFAULT_MAP_HANDLER = new MapHandler();

    /**
     * a
     */
    public final Supplier<Map<String, Object>> mapSupplier;

    public MapHandler() {
        this.mapSupplier = HashMap::new;
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
