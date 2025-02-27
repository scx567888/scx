package cool.scx.jdbc.mapping.base;

import cool.scx.jdbc.mapping.DataType;

/// 用于手动编写 DataType
///
/// @author scx567888
/// @version 0.0.1
public class BaseDataType implements DataType {

    private String name;
    private Integer length;

    public BaseDataType() {
    }

    public BaseDataType(DataType oldDataType) {
        setName(oldDataType.name());
        setLength(oldDataType.length());
    }

    public BaseDataType(String name) {
        setName(name);
    }

    public BaseDataType(String name, Integer length) {
        setName(name);
        setLength(length);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Integer length() {
        return length;
    }

    public BaseDataType setName(String name) {
        this.name = name;
        return this;
    }

    public BaseDataType setLength(Integer length) {
        this.length = length;
        return this;
    }

}
