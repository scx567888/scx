package cool.scx.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 抽象的 占位符 SQL
 *
 * @author scx567888
 * @version 1.11.8
 */
abstract class AbstractPlaceholderSQL {

    String normalSQL;

    /**
     * <p>normalSQL.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String normalSQL() {
        return normalSQL;
    }

    /**
     * 填充 PreparedStatement
     *
     * @param preparedStatement a
     * @param params            a
     * @throws java.sql.SQLException a
     */
    public final void fillPreparedStatement(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        var index = 1;
        for (var tempValue : params) {
            if (tempValue != null) {
                var tempValueClass = tempValue.getClass();
                //判断是否为数据库(MySQL)直接支持的数据类型
                var mysqlType = SQLTypeHelper.getMySQLType(tempValueClass);
                if (mysqlType != null) {
                    preparedStatement.setObject(index, tempValue, mysqlType);
                } else if (tempValueClass.isEnum()) {//不是则转换做一下特殊处理 枚举我们直接存名称
                    preparedStatement.setString(index, SQLTypeHelper.convertToStringOrNull(tempValue));
                } else {//否则存 json
                    preparedStatement.setString(index, SQLTypeHelper.convertToJsonOrNull(tempValue));
                }
            } else {
                //这里的 Types.NULL 其实内部并没有使用
                preparedStatement.setNull(index, Types.NULL);
            }
            index = index + 1;
        }
    }

    /**
     * 获取 PreparedStatement
     *
     * @param con a
     * @return a
     * @throws java.sql.SQLException a
     */
    public abstract PreparedStatement getPreparedStatement(Connection con) throws SQLException;

}
