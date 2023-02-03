package cool.scx.sql;

/**
 * TableInfo
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface TableInfo<T extends ColumnInfo> {

    String tableName();

    T[] columnInfos();

    T getColumnInfo(String javaFieldName);

}