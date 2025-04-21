package cool.scx.data.jdbc.column_name_mapping;

import cool.scx.data.jdbc.mapping.AnnotationConfigTable;

import java.util.function.Function;

/// 这个映射表示 列名 -> 字段
public class MapColumnNameMapping implements Function<String, String> {

    private final AnnotationConfigTable table;

    public MapColumnNameMapping(AnnotationConfigTable table) {
        this.table = table;
    }

    @Override
    public String apply(String columnName) {
        var column = this.table.getColumn(columnName);
        return column == null ? columnName : column.javaField().name();
    }

}
