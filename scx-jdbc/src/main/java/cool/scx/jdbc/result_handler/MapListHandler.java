package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.map_builder.MapBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cool.scx.jdbc.result_handler.MapHandler.createColumnLabelIndex;

/// MapListHandler
///
/// @author scx567888
/// @version 0.0.1
record MapListHandler(MapBuilder mapBuilder) implements ResultHandler<List<Map<String, Object>>, RuntimeException> {

    static final MapListHandler INSTANCE = new MapListHandler(MapBuilder.of());

    @Override
    public List<Map<String, Object>> apply(ResultSet rs, Dialect dialect) throws SQLException {
        var list = new ArrayList<Map<String, Object>>();
        var columnLabelIndex = createColumnLabelIndex(rs);
        while (rs.next()) {
            var map = mapBuilder.createMap(rs, columnLabelIndex);
            list.add(map);
        }
        return list;
    }

}
