package cool.scx.dao;

import com.mysql.cj.MysqlType;
import cool.scx.sql.mapping.ColumnInfo;
import cool.scx.sql.mapping.TableInfo;

import javax.sql.DataSource;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Dialect {

    /**
     * 获取建表语句
     *
     * @return s
     */
    public  String getCreateTableDDL(TableInfo<?> tableInfo);

    boolean canHandle(DataSource dataSource);

    /**
     * todo
     * @param nonExistentColumnNames
     * @param s
     */
    String getAlertTableDDL(List<? extends ColumnInfo> nonExistentColumnNames, String s);


    /**
     * 根据 class 获取对应的 SQLType 类型 如果没有则返回 JSON
     *
     * @param javaType 需要获取的类型
     * @return a {@link String} object.
     */
    public  String getSQLTypeCreateName(Class<?> javaType);

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
    public SQLType getSQLType(Class<?> javaType);

}
