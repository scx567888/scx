package cool.scx.sql.result_handler;

import cool.scx.functional.ScxHandlerR;
import cool.scx.sql.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
    public final ScxHandlerR<Map<String, Object>> mapSupplier;

    public MapHandler() {
        this.mapSupplier = HashMap::new;
    }

    public MapHandler(ScxHandlerR<Map<String, Object>> mapSupplier) {
        this.mapSupplier = mapSupplier;
    }

    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        var rsm = rs.getMetaData();
        var count = rsm.getColumnCount();
        if (rs.next()) {
            var map = this.mapSupplier.handle();
            for (int i = 1; i <= count; i = i + 1) {
                map.put(rsm.getColumnLabel(i), rs.getObject(i));
            }
            return map;
        } else {
            return null;
        }
    }

}
