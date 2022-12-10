package cool.scx.sql.result_handler;

import cool.scx.functional.ScxHandlerR;
import cool.scx.sql.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class MapListHandler implements ResultHandler<List<Map<String, Object>>> {

    /**
     * Constant <code>DEFAULT_MAP_LIST_HANDLER</code>
     */
    public static final MapListHandler DEFAULT_MAP_LIST_HANDLER = new MapListHandler();

    /**
     * a
     */
    private final ScxHandlerR<Map<String, Object>> mapSupplier;

    /**
     * <p>Constructor for MapListHandler.</p>
     */
    public MapListHandler() {
        this.mapSupplier = HashMap::new;
    }

    /**
     * a
     *
     * @param mapSupplier a
     */
    public MapListHandler(ScxHandlerR<Map<String, Object>> mapSupplier) {
        this.mapSupplier = mapSupplier;
    }

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {
        var list = new ArrayList<Map<String, Object>>();
        var rsm = rs.getMetaData();
        var count = rsm.getColumnCount();
        while (rs.next()) {
            var map = mapSupplier.handle();
            for (int i = 1; i <= count; i = i + 1) {
                map.put(rsm.getColumnLabel(i), rs.getObject(i));
            }
            list.add(map);
        }
        return list;
    }

}
