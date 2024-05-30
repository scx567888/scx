package cool.scx.jdbc.mysql;

import cool.scx.common.standard.JDBCType;
import cool.scx.jdbc.dialect.DDLBuilder;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.common.util.StringUtils.notBlank;

/**
 * @see <a href="https://dev.mysql.com/doc/refman/8.0/en/create-table.html">https://dev.mysql.com/doc/refman/8.0/en/create-table.html</a>
 */
public class MySQLDDLBuilder implements DDLBuilder {

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
    public String getDataTypeNameByJDBCType(JDBCType dataType) {
        var mysqlType = MySQLDialectHelper.jdbcTypeToDialectDataType(dataType);
        return mysqlType.getName();
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
    public String defaultDateType() {
        return "VARCHAR(128)";
    }

}
