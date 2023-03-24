package cool.scx.dao.schema;

import cool.scx.dao.mapping.ColumnInfo;
import cool.scx.dao.mapping.TableInfo;

public class PhysicsTableInfo implements TableInfo {

    @Override
    public String tableName() {
        return null;
    }

    @Override
    public ColumnInfo[] columnInfos() {
        return new ColumnInfo[0];
    }

    @Override
    public ColumnInfo getColumnInfo(String javaFieldName) {
        return null;
    }
}
