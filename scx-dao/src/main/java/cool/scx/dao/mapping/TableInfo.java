package cool.scx.dao.mapping;

/**
 * TableInfo
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface TableInfo<T extends ColumnInfo> {

    String tableName();

    T[] columnInfos();

    /**
     * 根据 javaFieldName 取值, 建议同时也支持 columnName 取值 不过优先级要小于 javaFieldName
     *
     * @param javaFieldName j
     * @return j
     */
    T getColumnInfo(String javaFieldName);

}