package cool.scx.jdbc.result_handler;

import cool.scx.function.Function1Void;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.result_handler.map_builder.MapBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static cool.scx.jdbc.result_handler.MapHandler.createColumnLabelIndex;

/// MapConsumerHandler
///
/// @author scx567888
/// @version 0.0.1
record MapConsumerHandler<X extends Throwable>(MapBuilder mapBuilder,
                                               Function1Void<Map<String, Object>, X> consumer) implements ResultHandler<Void, X> {

    @Override
    public Void apply(ResultSet rs, Dialect dialect) throws SQLException, X {
        var columnLabelIndex = createColumnLabelIndex(rs);
        while (rs.next()) {
            var map = mapBuilder.createMap(rs, columnLabelIndex);
            consumer.apply(map);
        }
        return null;
    }

}
