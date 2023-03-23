package cool.scx.dao.dialect;

import cool.scx.dao.Dialect;
import cool.scx.sql.mapping.ColumnInfo;
import cool.scx.sql.mapping.TableInfo;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.util.StringUtils.notBlank;

public class SQLiteDialect implements Dialect {

    @Override
    public String getCreateTableDDL(TableInfo<?> tableInfo) {
        var createTableDDL = new ArrayList<String>();
        var columnInfos = tableInfo.columnInfos();
        var tableName = tableInfo.tableName();
        for (var columnInfo : columnInfos) {
            var normalDDL = initNormalDDL(columnInfo);
            createTableDDL.add(normalDDL);
        }
        for (var columnInfo : columnInfos) {
            var specialDDL = initSpecialDDL(columnInfo);
            createTableDDL.addAll(List.of(specialDDL));
        }
        return "CREATE TABLE `" + tableName + "` (" + String.join(", ", createTableDDL) + ");";
    }

    @Override
    public boolean canHandle(DataSource dataSource) {
        try {
            return dataSource.isWrapperFor(SQLiteDataSource.class);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String getAlertTableDDL(List<? extends ColumnInfo> nonExistentColumnNames, String s) {
        return null;
    }

    @Override
    public String getSQLTypeCreateName(Class<?> javaType) {
        return null;
    }

    @Override
    public SQLType getSQLType(Class<?> javaType) {
        return null;
    }

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    private static String initNormalDDL(ColumnInfo column) {
        var tempList = new ArrayList<String>();
        tempList.add("`" + column.columnName() + "`");
        tempList.add(column.type());
        tempList.add(column.notNull() || column.primaryKey() ? "NOT NULL" : "NULL");
        if (column.autoIncrement()) {
            tempList.add("AUTO_INCREMENT");
        }
        if (notBlank(column.defaultValue())) {
            tempList.add("DEFAULT " + column.defaultValue());
        }
        if (notBlank(column.onUpdateValue())) {
            tempList.add("ON UPDATE " + column.onUpdateValue());
        }
        return String.join(" ", tempList);
    }


    /**
     * 当前列对象特殊的 DDL 如设置是否为主键 是否创建索引 是否是唯一值 (建表语句片段 , 需和 normalDDL 一起使用才完整)
     */
    public static String[] initSpecialDDL(ColumnInfo column) {
        if (column == null) {
            return new String[0];
        }
        var name = column.columnName();
        var list = new ArrayList<String>();
        if (column.primaryKey()) {
            list.add("PRIMARY KEY (`" + name + "`)");
        }
        if (column.unique()) {
            list.add("UNIQUE KEY `unique_" + name + "`(`" + name + "`)");
        }
        if (column.needIndex()) {
            list.add("KEY `index_" + name + "`(`" + name + "`)");
        }
        return list.toArray(String[]::new);
    }

}
