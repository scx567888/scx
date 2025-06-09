package cool.scx.data.jdbc.mapping;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.mapping.DataType;
import cool.scx.jdbc.mapping.type.TypeDataType;

import java.lang.reflect.Type;

import static cool.scx.data.jdbc.mapping.AnnotationConfigHelper.getDataTypeByJavaType;
import static cool.scx.jdbc.JDBCType.VARCHAR;

/// AnnotationConfigDataType
///
/// @author scx567888
/// @version 0.0.1
public class AnnotationConfigDataType implements TypeDataType {

    private static final int DEFAULT_VARCHAR_LENGTH = 128;

    private final JDBCType jdbcType;
    private final String name;
    private final Integer length;

    public AnnotationConfigDataType(cool.scx.data.jdbc.annotation.DataType dataType) {
        this.jdbcType = dataType.value();
        this.name = this.jdbcType.name();
        var _length = dataType.length() == -1 ? null : dataType.length();
        if (_length == null && this.jdbcType == VARCHAR) {
            _length = DEFAULT_VARCHAR_LENGTH;
        }
        this.length = _length;
    }

    public AnnotationConfigDataType(Type javaType) {
        this.jdbcType = getDataTypeByJavaType(javaType);
        this.name = this.jdbcType.name();
        this.length = this.jdbcType == VARCHAR ? DEFAULT_VARCHAR_LENGTH : null;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Integer length() {
        return this.length;
    }

    @Override
    public JDBCType jdbcType() {
        return this.jdbcType;
    }

}
