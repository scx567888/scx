package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.map_builder.MapBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/// MapHandler
///
/// @author scx567888
/// @version 0.0.1
record MapHandler(MapBuilder mapBuilder) implements ResultHandler<Map<String, Object>, RuntimeException> {

    static final MapHandler INSTANCE = new MapHandler(MapBuilder.of());

    public static String[] createColumnLabelIndex(ResultSet rs) throws SQLException {
        var rsm = rs.getMetaData();
        var count = rsm.getColumnCount();
        var columnLabelIndex = new String[count + 1];// + 1是为了匹配 jdbc 索引从 1 开始 实际上第一位永远为 null
        for (int i = 1; i <= count; i = i + 1) {
            columnLabelIndex[i] = rsm.getColumnLabel(i);
        }
        return columnLabelIndex;
    }

    @Override
    public Map<String, Object> apply(ResultSet rs, Dialect dialect) throws SQLException {
        var columnLabelIndex = createColumnLabelIndex(rs);
        if (rs.next()) {
            return mapBuilder.createMap(rs, columnLabelIndex);
        } else {
            return null;
        }
    }

}
