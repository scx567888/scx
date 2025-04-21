package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.map_builder.MapBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Consumer;

import static cool.scx.jdbc.result_handler.MapHandler.createColumnLabelIndex;

/// MapConsumerHandler
///
/// @author scx567888
/// @version 0.0.1
record MapConsumerHandler(MapBuilder mapBuilder,
                          Consumer<Map<String, Object>> consumer) implements ResultHandler<Void> {

    @Override
    public Void apply(ResultSet rs, Dialect dialect) throws SQLException {
        var columnLabelIndex = createColumnLabelIndex(rs);
        while (rs.next()) {
            var map = mapBuilder.createMap(rs, columnLabelIndex);
            consumer.accept(map);
        }
        return null;
    }

}
