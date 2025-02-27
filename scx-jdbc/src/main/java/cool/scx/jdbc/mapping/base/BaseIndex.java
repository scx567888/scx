package cool.scx.jdbc.mapping.base;

import cool.scx.jdbc.mapping.Index;

/// 用于手动编写 Index
///
/// @author scx567888
/// @version 0.0.1
public class BaseIndex implements Index {

    private String name;
    private String columnName;
    private boolean unique;

    public BaseIndex() {
    }

    public BaseIndex(Index oldIndex) {
        setName(oldIndex.name());
        setColumnName(oldIndex.columnName());
        setUnique(oldIndex.unique());
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String columnName() {
        return columnName;
    }

    @Override
    public boolean unique() {
        return unique;
    }

    public BaseIndex setName(String name) {
        this.name = name;
        return this;
    }

    public BaseIndex setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public BaseIndex setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

}
