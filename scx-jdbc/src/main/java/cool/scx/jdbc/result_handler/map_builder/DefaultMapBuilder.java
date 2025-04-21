package cool.scx.jdbc.result_handler.map_builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

record DefaultMapBuilder(Supplier<Map<String, Object>> mapSupplier,
                         Function<String, String> columnNameMapping) implements MapBuilder {

    public static final MapBuilder MAP_BUILDER = new DefaultMapBuilder(HashMap::new, c -> c);

    public Map<String, Object> createMap(ResultSet rs, String[] columnLabelIndex) throws SQLException {
        var map = mapSupplier.get();

        for (int i = 1; i < columnLabelIndex.length; i = i + 1) {
            var key = columnNameMapping.apply(columnLabelIndex[i]);
            var value = rs.getObject(i);
            map.put(key, value);
        }

        return map;
    }

}
