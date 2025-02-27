package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/// MapConsumerHandler
///
/// @author scx567888
/// @version 0.0.1
record MapConsumerHandler(Supplier<Map<String, Object>> mapSupplier,
                          Consumer<Map<String, Object>> consumer) implements ResultHandler<Void> {

    public MapConsumerHandler(Consumer<Map<String, Object>> consumer) {
        this(HashMap::new, consumer);
    }

    @Override
    public Void apply(ResultSet rs, Dialect dialect) throws SQLException {
        var rsm = rs.getMetaData();
        var count = rsm.getColumnCount();
        while (rs.next()) {
            var map = mapSupplier.get();
            for (int i = 1; i <= count; i = i + 1) {
                map.put(rsm.getColumnLabel(i), rs.getObject(i));
            }
            consumer.accept(map);
        }
        return null;
    }

}
