package cool.scx.jdbc.dialect._default;

import cool.scx.common.standard.JDBCType;
import cool.scx.jdbc.dialect.DDLBuilder;

public final class DefaultDDLBuilder implements DDLBuilder {

    public static final DDLBuilder DEFAULT_DDL_BUILDER = new DefaultDDLBuilder();

    private DefaultDDLBuilder() {
    }

    @Override
    public String getDataTypeNameByJDBCType(JDBCType dataType) {
        return dataType.name();
    }

}
