package cool.scx.jdbc.dialect._default;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.dialect.DDLBuilder;

/// DefaultDDLBuilder
///
/// @author scx567888
/// @version 0.0.1
public final class DefaultDDLBuilder implements DDLBuilder {

    public static final DDLBuilder DEFAULT_DDL_BUILDER = new DefaultDDLBuilder();

    private DefaultDDLBuilder() {
    }

    @Override
    public String getDataTypeNameByJDBCType(JDBCType dataType) {
        return dataType.name();
    }

}
