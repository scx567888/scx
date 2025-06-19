package cool.scx.jdbc.result_handler;

import cool.scx.functional.ScxConsumer;
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
record MapConsumerHandler<E extends Throwable>(MapBuilder mapBuilder,
                          ScxConsumer<Map<String, Object>,E> consumer) implements ResultHandler<Void,E> {

    @Override
    public Void apply(ResultSet rs, Dialect dialect) throws SQLException, E {
        var columnLabelIndex = createColumnLabelIndex(rs);
        while (rs.next()) {
            var map = mapBuilder.createMap(rs, columnLabelIndex);
            consumer.accept(map);
        }
        return null;
    }

}
