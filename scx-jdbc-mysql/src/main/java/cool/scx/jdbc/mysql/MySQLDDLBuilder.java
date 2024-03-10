package cool.scx.jdbc.mysql;

import com.mysql.cj.MysqlType;
import cool.scx.jdbc.dialect.DDLBuilder;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.standard.StandardDataType;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.util.StringUtils.notBlank;

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
    public String getDataTypeDefinitionByStandardDataType(StandardDataType dataType) {
        var mysqlType = switch (dataType) {
            case TINYINT -> MysqlType.TINYINT;
            case SMALLINT -> MysqlType.SMALLINT;
            case INT -> MysqlType.INT;
            case BIGINT -> MysqlType.BIGINT;
            case FLOAT -> MysqlType.FLOAT;
            case DOUBLE -> MysqlType.DOUBLE;
            case BOOLEAN -> MysqlType.BOOLEAN;
            case DECIMAL -> MysqlType.DECIMAL;
            case DATE -> MysqlType.DATE;
            case TIME -> MysqlType.TIME;
            case DATETIME -> MysqlType.DATETIME;
            case VARCHAR -> MysqlType.VARCHAR;
            case TEXT -> MysqlType.TEXT;
            case LONGTEXT -> MysqlType.LONGTEXT;
            case BINARY -> MysqlType.BINARY;
            case JSON -> MysqlType.JSON;
        };
        return mysqlType == MysqlType.VARCHAR ? mysqlType.getName() + "(128)" : mysqlType.getName();
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
