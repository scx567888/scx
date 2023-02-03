package cool.scx.sql.result_handler;

import cool.scx.sql.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class MapListHandler implements ResultHandler<List<Map<String, Object>>> {

    private final Supplier<Map<String, Object>> mapSupplier;

    public MapListHandler() {
        this(HashMap::new);
    }

    public MapListHandler(Supplier<Map<String, Object>> mapSupplier) {
        this.mapSupplier = mapSupplier;
    }

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public List<Map<String, Object>> apply(ResultSet rs) throws SQLException {
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
