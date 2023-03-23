package cool.scx.dao.dialect;

import com.mysql.cj.MysqlType;
import com.mysql.cj.NativeQueryBindings;
import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.dao.Dialect;
import cool.scx.sql.mapping.ColumnInfo;
import cool.scx.sql.mapping.TableInfo;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cool.scx.util.StringUtils.notBlank;

public class MySQLDialect implements Dialect {

    private static final Map<Class<?>, MysqlType> DEFAULT_MYSQL_TYPES = initDefaultMySQLTypes();

    @SuppressWarnings("unchecked")
    private static Map<Class<?>, MysqlType> initDefaultMySQLTypes() {
        var tempMap = new HashMap<Class<?>, MysqlType>();
        //这里 我们在额外添加几个下表对应的基本类型或包装类型
        tempMap.put(byte.class, MysqlType.TINYINT);
        tempMap.put(Byte[].class, MysqlType.BINARY);
        tempMap.put(double.class, MysqlType.DOUBLE);
        tempMap.put(float.class, MysqlType.FLOAT);
        tempMap.put(int.class, MysqlType.INT);
        tempMap.put(long.class, MysqlType.BIGINT);
        tempMap.put(short.class, MysqlType.SMALLINT);
        tempMap.put(boolean.class, MysqlType.BOOLEAN);

        try {
            //整合 mysql 驱动中的 DEFAULT_MYSQL_TYPES
            var f = NativeQueryBindings.class.getDeclaredField("DEFAULT_MYSQL_TYPES");
            f.setAccessible(true);
            var mysqlDriverDefaultMysqlTypes = (Map<Class<?>, MysqlType>) f.get(null);
            tempMap.putAll(mysqlDriverDefaultMysqlTypes);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return tempMap;
    }

    public  String getCreateTableDDL(TableInfo<?> tableInfo) {
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
            return dataSource.isWrapperFor(MysqlDataSource.class);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    private static String initNormalDDL(ColumnInfo column) {
        var tempList = new ArrayList<String>();
        tempList.add("`" + column.columnName() + "`");
        //todo column.type(); 只是自定义的 type 我们需要根据方言重新设置 type
        column.type();
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


    /**
     * 获取修复表的语句
     *
     * @param nonExistentColumnName java 字段的名称 (注意 : fieldNames 中存在但 allFields 中不存在的则会忽略)
     * @return a
     */
    public  String getAlertTableDDL(List<? extends ColumnInfo> nonExistentColumnName, String tableName) {
        var alertTableDDL = new ArrayList<String>();
        for (var field : nonExistentColumnName) {
            var normalDDL = initNormalDDL(field);
            alertTableDDL.add("ADD " + normalDDL);
        }
        for (var s : nonExistentColumnName) {
            var specialDDL = initSpecialDDL(s);
            for (var s1 : specialDDL) {
                alertTableDDL.add("ADD " + s1);
            }
        }
        return "ALTER TABLE `" + tableName + "` " + String.join(", ", alertTableDDL) + ";";
    }

    @Override
    public String getSQLTypeCreateName(Class<?> javaType) {
        var mysqlType = getSQLType(javaType);
        if (mysqlType == null) {
            if (javaType.isEnum()) {
                mysqlType = MysqlType.VARCHAR;
            } else {
                mysqlType = MysqlType.JSON;
            }
        }
        return mysqlType == MysqlType.VARCHAR ? mysqlType.getName() + "(128)" : mysqlType.getName();
    }

    @Override
    public SQLType getSQLType(Class<?> javaType) {
        var mysqlType = DEFAULT_MYSQL_TYPES.get(javaType);
        if (mysqlType == null) {
            return DEFAULT_MYSQL_TYPES.entrySet().stream()
                    .filter(entry -> entry.getKey().isAssignableFrom(javaType))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(null);
        }
        return mysqlType;
    }

}
