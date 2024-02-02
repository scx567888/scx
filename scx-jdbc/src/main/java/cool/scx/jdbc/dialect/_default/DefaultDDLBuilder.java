package cool.scx.jdbc.dialect._default;

import cool.scx.jdbc.dialect.DDLBuilder;

public final class DefaultDDLBuilder implements DDLBuilder {

    public static final DDLBuilder DEFAULT_DDL_BUILDER = new DefaultDDLBuilder();

    private DefaultDDLBuilder() {
    }

    @Override
    public String getDataTypeDefinitionByClass(Class<?> javaType) {
        return null;
    }

}
