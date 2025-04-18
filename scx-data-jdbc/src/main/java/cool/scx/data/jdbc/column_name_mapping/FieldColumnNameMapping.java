package cool.scx.data.jdbc.column_name_mapping;

import cool.scx.jdbc.mapping.Table;

import java.lang.reflect.Field;
import java.util.function.Function;

public class FieldColumnNameMapping implements Function<Field, String> {

    private final Table table;

    public FieldColumnNameMapping(Table table) {
        this.table = table;
    }

    @Override
    public String apply(Field field) {
        var column = this.table.getColumn(field.getName());
        return column == null ? field.getName() : column.name();
    }

}
