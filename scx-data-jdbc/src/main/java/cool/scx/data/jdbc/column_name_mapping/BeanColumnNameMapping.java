package cool.scx.data.jdbc.column_name_mapping;

import cool.scx.jdbc.mapping.Table;

import java.lang.reflect.Field;
import java.util.function.Function;

/// 这个映射表示 字段 -> 列名
public class BeanColumnNameMapping implements Function<Field, String> {

    private final Table table;

    public BeanColumnNameMapping(Table table) {
        this.table = table;
    }

    @Override
    public String apply(Field field) {
        var column = this.table.getColumn(field.getName());
        return column == null ? null : column.name();
    }

}
