package cool.scx.sql;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.MysqlType;
import com.mysql.cj.NativeQueryBindings;
import com.mysql.cj.PreparedQuery;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import cool.scx.sql.type_handler.JdbcType;
import cool.scx.util.ObjectUtils;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;



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



    @SuppressWarnings("unchecked")
    private static Map<Class<?>, MysqlType> initDefaultMySQLTypes() {

        var tempMap = new HashMap<Class<?>, MysqlType>();
        //这里 我们在额外添加几个下表对应的基本类型或包装类型



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
//
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
//
//    /**
//     * 填充 PreparedStatement
//     *
//     * @param preparedStatement a
//     * @param params            a
//     * @throws SQLException a
//     */
    public static void fillPreparedStatement(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        var index = 1;
        for (var tempValue : params) {
            if (tempValue != null) {
                var tempValueClass = tempValue.getClass();
                TypeHandler<Object> typeHandler = TypeHandlerRegistry.getTypeHandler(tempValueClass);
                typeHandler.setObject(preparedStatement,index,tempValue, null);
            } else {
                //这里的 Types.NULL 其实内部并没有使用
                preparedStatement.setNull(index, Types.NULL);
            }
            index = index + 1;
        }
    }

}
