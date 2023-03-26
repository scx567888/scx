package cool.scx.dao.mapping;

import cool.scx.sql.mapping.TableMapping;
import cool.scx.sql.meta_data.PrimaryKeyMetaData;

/**
 * TableInfo
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface TableInfo<T extends ColumnInfo> extends TableMapping<T, PrimaryKeyMetaData> {

}