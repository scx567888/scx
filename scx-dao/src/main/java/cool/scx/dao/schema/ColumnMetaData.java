package cool.scx.dao.schema;

import java.sql.DatabaseMetaData;
import java.util.Objects;

public class ColumnMetaData {

    private final TableMetaData table;
    private final String columnName;
    private final String typeName;
    private final Integer columnSize;
    private final boolean isNullable;
    private final boolean isAutoincrement;
    private final TableMetaData._Column _column;
    private final String remarks;

    public ColumnMetaData(TableMetaData tableMetaData, DatabaseMetaData dbMetaData, TableMetaData._Column column) {
        this.table = tableMetaData;
        this.columnName = column.COLUMN_NAME();
        this.typeName = column.TYPE_NAME();
        this.columnSize = column.COLUMN_SIZE();
        this.remarks = column.REMARKS();
        this.isNullable = Objects.equals("YES", column.IS_NULLABLE());
        this.isAutoincrement = Objects.equals("YES", column.IS_AUTOINCREMENT());
        this._column = column;
    }

    public String columnName() {
        return columnName;
    }

    public String typeName() {
        return typeName;
    }
}
