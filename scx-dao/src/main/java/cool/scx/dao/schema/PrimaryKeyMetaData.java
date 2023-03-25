package cool.scx.dao.schema;

public record PrimaryKeyMetaData(String columnName, _PrimaryKey _primaryKey) {

    public static PrimaryKeyMetaData of(_PrimaryKey _primaryKey) {
        var columnName = _primaryKey.COLUMN_NAME();
        return new PrimaryKeyMetaData(columnName, _primaryKey);
    }

    public record _PrimaryKey(String TABLE_CAT, String TABLE_SCHEM, String TABLE_NAME, String COLUMN_NAME,
                              Short KEY_SEQ, String PK_NAME) {

    }

}
