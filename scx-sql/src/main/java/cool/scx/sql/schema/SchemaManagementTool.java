package cool.scx.sql.schema;

import cool.scx.sql.mapping.ColumnInfo;
import cool.scx.sql.mapping.TableInfo;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.sql.SQLHelper.initSpecialDDL;
import static cool.scx.util.StringUtils.notBlank;

/**
 * todo 数据库 DDL 创建工具
 */
public final class SchemaManagementTool {

    public static String getCreateTableDDL(TableInfo newTable) {
        var createTableDDL = new ArrayList<String>();
        var columnInfos = newTable.columnInfos();
        var tableName = newTable.tableName();
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

    public static String getMigrateSQL(TableInfo oldTable, TableInfo newTable) {
        return "";
    }

    public static SchemaVerifyResult verify(TableInfo oldTable, TableInfo newTable) {
        return new SchemaVerifyResult();
    }

}
