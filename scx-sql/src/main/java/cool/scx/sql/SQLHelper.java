package cool.scx.sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.MysqlType;
import com.mysql.cj.NativeQueryBindings;
import com.mysql.cj.PreparedQuery;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import cool.scx.util.CaseUtils;
import cool.scx.util.ObjectUtils;
import org.slf4j.Logger;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.util.StringUtils.notBlank;

/**
 * 构建 SQL 的助手(常用方法) 类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class SQLHelper {

    /**
     * 这里我们和 SQLRunner 公用一个 logger 方便管理
     */
    private static final Logger logger = SQLRunner.logger;

    private static final Map<Class<?>, MysqlType> DEFAULT_MYSQL_TYPES = initDefaultMySQLTypes();

    private static final ObjectMapper objectMapper = ObjectUtils.jsonMapper(ObjectUtils.Option.IGNORE_JSON_IGNORE);

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

    /**
     * 根据 class 获取对应的 SQLType 类型 如果没有则返回 JSON
     *
     * @param javaType 需要获取的类型
     * @return a {@link String} object.
     */
    public static String getMySQLTypeCreateName(Class<?> javaType) {
        var mysqlType = getMySQLType(javaType);
        if (mysqlType == null) {
            if (javaType.isEnum()) {
                mysqlType = MysqlType.VARCHAR;
            } else {
                mysqlType = MysqlType.JSON;
            }
        }
        return mysqlType == MysqlType.VARCHAR ? mysqlType.getName() + "(128)" : mysqlType.getName();
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
    public static MysqlType getMySQLType(Class<?> javaType) {
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

    /**
     * a
     *
     * @param o a
     * @return a
     */
    public static String convertToStringOrNull(Object o) {
        try {
            return objectMapper.convertValue(o, String.class);
        } catch (Exception e) {
            logger.error("序列化时发生错误 , 已使用 NULL !!!", e);
            return null;
        }
    }

    /**
     * a
     *
     * @param o a
     * @return a
     */
    public static String convertToJsonOrNull(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            logger.error("序列化时发生错误 , 已使用 NULL !!!", e);
            return null;
        }
    }

    /**
     * a
     *
     * @param o         a
     * @param filedType a
     * @return a
     */
    public static Object readFromValueOrNull(String o, Class<?> filedType) {
        if (o != null) {
            try {
                return objectMapper.convertValue(o, filedType);
            } catch (Exception e) {
                logger.error("反序列化时发生错误 , 已使用 NULL !!!", e);
            }
        }
        return null;
    }

    /**
     * 读取 json 值 或者返回 null
     *
     * @param json        s
     * @param genericType g
     * @return r
     */
    public static Object readFromJsonValueOrNull(String json, Type genericType) {
        if (json != null) {
            try {
                return objectMapper.readValue(json, ObjectUtils.constructType(genericType));
            } catch (Exception e) {
                logger.error("反序列化时发生错误 , 已使用 NULL !!!", e);
            }
        }
        return null;
    }

    /**
     * a
     *
     * @param name            a
     * @param useJsonExtract  a
     * @param useOriginalName a
     * @return a
     */
    public static String getColumnName(String name, boolean useJsonExtract, boolean useOriginalName) {
        if (useJsonExtract) {
            var c = splitIntoColumnNameAndFieldPath(name);
            if (notBlank(c.columnName()) && notBlank(c.fieldPath())) {
                var jsonQueryColumnName = useOriginalName ? c.columnName() : CaseUtils.toSnake(c.columnName());
                return jsonQueryColumnName + " -> " + "'$" + c.fieldPath() + "'";
            } else {
                throw new IllegalArgumentException("使用 USE_JSON_EXTRACT 时, 查询名称不合法 !!! 字段名 : " + name);
            }
        } else {// 这里就是普通的判断一下是否使用 原始名称即可
            return useOriginalName ? name : CaseUtils.toSnake(name);
        }
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public static ColumnNameAndFieldPath splitIntoColumnNameAndFieldPath(String name) {
        var charArray = name.toCharArray();
        var index = charArray.length;
        for (int i = 0; i < charArray.length; i = i + 1) {
            var c = charArray[i];
            if (c == '.' || c == '[') {
                index = i;
                break;
            }
        }
        var columnName = name.substring(0, index);
        var fieldPath = name.substring(index);
        return new ColumnNameAndFieldPath(columnName, fieldPath);
    }

    /**
     * 　获取最终的 SQL
     *
     * @param preparedStatement a
     * @return a
     */
    public static String getFinalSQL(PreparedStatement preparedStatement) {
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
     * 打印 SQL
     *
     * @param p a
     * @return 方便函数式调用
     */
    public static PreparedStatement logSQL(PreparedStatement p) {
        if (logger.isDebugEnabled()) {
            logger.debug(SQLHelper.getFinalSQL(p));
        }
        return p;
    }

    /**
     * 填充 PreparedStatement
     *
     * @param preparedStatement a
     * @param params            a
     * @throws SQLException a
     */
    public static void fillPreparedStatement(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        var index = 1;
        for (var tempValue : params) {
            if (tempValue != null) {
                var tempValueClass = tempValue.getClass();
                //判断是否为数据库(MySQL)直接支持的数据类型
                var mysqlType = SQLHelper.getMySQLType(tempValueClass);
                if (mysqlType != null) {
                    preparedStatement.setObject(index, tempValue, mysqlType);
                } else if (tempValueClass.isEnum()) {//不是则转换做一下特殊处理 枚举我们直接存名称
                    preparedStatement.setString(index, SQLHelper.convertToStringOrNull(tempValue));
                } else {//否则存 json
                    preparedStatement.setString(index, SQLHelper.convertToJsonOrNull(tempValue));
                }
            } else {
                //这里的 Types.NULL 其实内部并没有使用
                preparedStatement.setNull(index, Types.NULL);
            }
            index = index + 1;
        }
    }

}
