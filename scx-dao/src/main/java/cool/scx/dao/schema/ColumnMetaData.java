package cool.scx.dao.schema;

import java.sql.DatabaseMetaData;
import java.util.Objects;

public class ColumnMetaData {

    private final TableMetaData table;
    private final String columnName;
    private final String typeName;
    private final Integer columnSize;
    private final Boolean isNullable;
    private final Boolean isAutoincrement;
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

    public Integer columnSize() {
        return columnSize;
    }

    public Boolean isAutoincrement() {
        return isAutoincrement;
    }

    public Boolean isNullable() {
        return isNullable;
    }

    public String remarks() {
        return remarks;
    }

}
