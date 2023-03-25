package cool.scx.dao.schema;

import java.sql.DatabaseMetaData;

public class PrimaryKeyMetaData {

    private final TableMetaData._PrimaryKey _primaryKey;
    private final String columnName;

    public PrimaryKeyMetaData(TableMetaData tableMetaData, DatabaseMetaData dbMetaData, TableMetaData._PrimaryKey primaryKey) {
        this.columnName = primaryKey.COLUMN_NAME();
        this._primaryKey = primaryKey;
    }

    public String columnName() {
        return columnName;
    }

}
