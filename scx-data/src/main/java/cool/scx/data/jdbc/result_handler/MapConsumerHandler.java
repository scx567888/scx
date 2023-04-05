package cool.scx.data.jdbc.result_handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
record MapConsumerHandler(Supplier<Map<String, Object>> mapSupplier,
                          Consumer<Map<String, Object>> consumer) implements ResultHandler<Void> {

    public MapConsumerHandler(Consumer<Map<String, Object>> consumer) {
        this(HashMap::new, consumer);
    }

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public Void apply(ResultSet rs) throws SQLException {
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

    @Override
    public PreparedStatement beforeExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setFetchSize(Integer.MIN_VALUE);
        return preparedStatement;
    }

}
