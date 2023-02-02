package cool.scx.dao;

import cool.scx.sql.TableInfo;

public abstract class BaseDaoTableInfo<T extends BaseDaoColumnInfo> implements TableInfo<T> {

    public abstract T getColumnInfo(String javaFieldName);

}
