package cool.scx.dao.schema;

import java.sql.DatabaseMetaData;
import java.util.List;

public class PrimaryKeyMetaData {

    private final List<TableMetaData._PrimaryKey> _primaryKey;

    public PrimaryKeyMetaData(TableMetaData tableMetaData, DatabaseMetaData dbMetaData, List<TableMetaData._PrimaryKey> primaryKey) {
        this._primaryKey = primaryKey;
    }

}
