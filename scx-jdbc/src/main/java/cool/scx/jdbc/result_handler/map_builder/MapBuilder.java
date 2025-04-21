package cool.scx.jdbc.result_handler.map_builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static cool.scx.jdbc.result_handler.map_builder.DefaultMapBuilder.MAP_BUILDER;

public interface MapBuilder {

    static MapBuilder of() {
        return MAP_BUILDER;
    }

    static MapBuilder of(Function<String, String> fieldNameMapping) {
        return new DefaultMapBuilder(HashMap::new, fieldNameMapping);
    }

    static MapBuilder of(Supplier<Map<String, Object>> mapSupplier) {
        return new DefaultMapBuilder(mapSupplier, c -> c);
    }

    static MapBuilder of(Supplier<Map<String, Object>> mapSupplier, Function<String, String> fieldNameMapping) {
        return new DefaultMapBuilder(mapSupplier, fieldNameMapping);
    }

    Map<String, Object> createMap(ResultSet rs, String[] columnLabelIndex) throws SQLException;

}
