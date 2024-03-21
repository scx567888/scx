package cool.scx.data.jdbc;

import cool.scx.data.jdbc.annotation.DataType;
import cool.scx.jdbc.mapping.type.TypeDataType;
import cool.scx.jdbc.standard.StandardDataType;

import static cool.scx.jdbc.standard.StandardDataType.JSON;
import static cool.scx.jdbc.standard.StandardDataType.VARCHAR;

public class AnnotationConfigDataType implements TypeDataType {

    private static final int DEFAULT_VARCHAR_LENGTH = 128;

    private final StandardDataType standardDataType;
    private final String name;
    private final Integer length;

    public AnnotationConfigDataType(DataType dataType) {
        this.standardDataType = dataType.value();
        this.name = this.standardDataType.name();
        var _length = dataType.length() == -1 ? null : dataType.length();
        if (_length == null && this.standardDataType == VARCHAR) {
            _length = DEFAULT_VARCHAR_LENGTH;
        }
        this.length = _length;
    }

    public AnnotationConfigDataType(Class<?> javaType) {
        var _standardDataType = StandardDataType.getByJavaType(javaType);
        if (_standardDataType == null) {
            if (javaType.isEnum()) {
                _standardDataType = VARCHAR;
            } else {
                _standardDataType = JSON;
            }
        }
        this.standardDataType = _standardDataType;
        this.name = this.standardDataType.name();
        this.length = this.standardDataType == VARCHAR ? DEFAULT_VARCHAR_LENGTH : null;
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
    public StandardDataType standardDataType() {
        return this.standardDataType;
    }

}
