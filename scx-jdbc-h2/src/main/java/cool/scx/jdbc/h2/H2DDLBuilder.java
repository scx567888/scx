package cool.scx.jdbc.h2;

import cool.scx.jdbc.dialect.DDLBuilder;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.util.StringUtils.notBlank;

public class H2DDLBuilder implements DDLBuilder {

    @Override
    public String getDataTypeDefinitionByClass(Class<?> javaType) {

        return "";
    }

    @Override
    public List<String> getTableConstraint(Table table) {
        var list = new ArrayList<String>();
        for (var column : table.columns()) {
            var name = column.name();
            if (column.primary()) {
                list.add("PRIMARY KEY (`" + name + "`)");
            }
            if (column.unique()) {
                list.add("UNIQUE KEY `unique_" + name + "`(`" + name + "`)");
            }
            if (column.index()) {
                list.add("KEY `index_" + name + "`(`" + name + "`)");
            }
        }
        return list;
    }


    @Override
    public List<String> getColumnConstraint(Column column) {
        var list = new ArrayList<String>();
        list.add(column.notNull() || column.primary() ? "NOT NULL" : "NULL");
        if (column.autoIncrement()) {
            list.add("AUTO_INCREMENT");
        }
        if (notBlank(column.defaultValue())) {
            list.add("DEFAULT " + column.defaultValue());
        }
        if (notBlank(column.onUpdate())) {
            list.add("ON UPDATE " + column.onUpdate());
        }
        return list;
    }

    @Override
    public String defaultDateType() {
        return "VARCHAR(128)";
    }

}
