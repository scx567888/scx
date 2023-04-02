package cool.scx.orm.jdbc.dialect;

import com.mysql.cj.MysqlType;
import com.mysql.cj.NativeQueryBindings;
import com.mysql.cj.PreparedQuery;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.orm.jdbc.mapping.Column;
import cool.scx.orm.jdbc.mapping.Table;
import cool.scx.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://dev.mysql.com/doc/refman/8.0/en/create-table.html">https://dev.mysql.com/doc/refman/8.0/en/create-table.html</a>
 */
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
     * 获取 mysql 类型
     * 用于后续判断类型是否可以由 JDBC 进行 SQLType 到 JavaType 的直接转换
     * <p>
     * 例子 :
     * String 可以由 varchar 直接转换 true
     * Integer 可以由 int 直接转换 true
     * User 不可以由 json 直接转换 false
     *
     * @param javaType 需要判断的类型
     * @return r
     */
    private static SQLType getSQLType(Class<?> javaType) {
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

    @Override
    public String getLimitSQL(String sql, Long offset, Long rowCount) {
        var limitClauses = rowCount == null ? "" : offset == null || offset == 0 ? " LIMIT " + rowCount : " LIMIT " + offset + "," + rowCount;
        return sql + limitClauses;
    }

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    @Override
    public List<String> getColumnConstraint(Column column) {
        var list = new ArrayList<String>();
        list.add(column.notNull() || column.primaryKey() ? "NOT NULL" : "NULL");
        if (column.autoIncrement()) {
            list.add("AUTO_INCREMENT");
        }
        if (StringUtils.notBlank(column.defaultValue())) {
            list.add("DEFAULT " + column.defaultValue());
        }
        if (StringUtils.notBlank(column.onUpdateValue())) {
            list.add("ON UPDATE " + column.onUpdateValue());
        }
        return list;
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
    public List<String> getTableConstraint(Table<?> table) {
        var list = new ArrayList<String>();
        for (var column : table.columns()) {
            var name = column.name();
            if (column.primaryKey()) {
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
