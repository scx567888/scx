package cool.scx.dao.dialect;

import com.mysql.cj.MysqlType;
import com.mysql.cj.NativeQueryBindings;
import com.mysql.cj.PreparedQuery;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.dao.Dialect;
import cool.scx.dao.mapping.ColumnInfo;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cool.scx.util.StringUtils.notBlank;
import static cool.scx.util.StringUtils.notEmpty;

public class MySQLDialect implements Dialect {

    private static final Map<Class<?>, MysqlType> DEFAULT_MYSQL_TYPES;

    static {
        DEFAULT_MYSQL_TYPES = new HashMap<>();
        //这里 我们在额外添加几个下表对应的基本类型或包装类型
        DEFAULT_MYSQL_TYPES.put(byte.class, MysqlType.TINYINT);
        DEFAULT_MYSQL_TYPES.put(Byte[].class, MysqlType.BINARY);
        DEFAULT_MYSQL_TYPES.put(double.class, MysqlType.DOUBLE);
        DEFAULT_MYSQL_TYPES.put(float.class, MysqlType.FLOAT);
        DEFAULT_MYSQL_TYPES.put(int.class, MysqlType.INT);
        DEFAULT_MYSQL_TYPES.put(long.class, MysqlType.BIGINT);
        DEFAULT_MYSQL_TYPES.put(short.class, MysqlType.SMALLINT);
        DEFAULT_MYSQL_TYPES.put(boolean.class, MysqlType.BOOLEAN);

        try {
            //整合 mysql 驱动中的 DEFAULT_MYSQL_TYPES
            var f = NativeQueryBindings.class.getDeclaredField("DEFAULT_MYSQL_TYPES");
            f.setAccessible(true);
            @SuppressWarnings("unchecked")
            var mysqlDriverDefaultMysqlTypes = (Map<Class<?>, MysqlType>) f.get(null);
            DEFAULT_MYSQL_TYPES.putAll(mysqlDriverDefaultMysqlTypes);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public boolean canHandle(DataSource dataSource) {
        try {
            return dataSource instanceof MysqlDataSource || dataSource.isWrapperFor(MysqlDataSource.class);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean canHandle(Driver driver) {
        return driver instanceof com.mysql.cj.jdbc.Driver;
    }

    @Override
    public String getFinalSQL(Statement preparedStatement) {
        ClientPreparedStatement clientPreparedStatement;
        try {
            clientPreparedStatement = preparedStatement.unwrap(ClientPreparedStatement.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        var preparedQuery = ((PreparedQuery) clientPreparedStatement.getQuery());
        var finalSQL = preparedQuery.asSql();
        var batchedArgsSize = preparedQuery.getBatchedArgs() == null ? 0 : preparedQuery.getBatchedArgs().size();
        return batchedArgsSize > 1 ? finalSQL + "... 额外的 " + (batchedArgsSize - 1) + " 项" : finalSQL;
    }

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    private String initNormalDDL(ColumnInfo column) {
        var tempList = new ArrayList<String>();
        tempList.add("`" + column.columnName() + "`");
        tempList.add(getDataTypeDefinition(column));
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

    @Override
    public List<String> getColumnDefinitions(ColumnInfo[] columnInfos) {
        var createTableDDL = new ArrayList<String>();
        for (var columnInfo : columnInfos) {
            var normalDDL = initNormalDDL(columnInfo);
            createTableDDL.add(normalDDL);
        }
        for (var columnInfo : columnInfos) {
            var specialDDL = initSpecialDDL(columnInfo);
            createTableDDL.addAll(List.of(specialDDL));
        }
        return createTableDDL;
    }

    @Override
    public String getDataTypeDefinitionByClass(Class<?> javaType) {
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

    public String getDataTypeDefinition(ColumnInfo column) {
        if (notEmpty(column.type())) {
            return column.type();
        } else {
            return getDataTypeDefinitionByClass(column.javaField().getType());
        }
    }

}
