package cool.scx.jdbc.mapping.base;

import cool.scx.jdbc.mapping.Key;

/// 用于手动编写 Key
///
/// @author scx567888
/// @version 0.0.1
public class BaseKey implements Key {

    private String name;
    private String columnName;
    private boolean primary;

    public BaseKey() {
    }

    public BaseKey(Key oldKey) {
        setName(oldKey.name());
        setColumnName(oldKey.columnName());
        setPrimary(oldKey.primary());
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
    public boolean primary() {
        return primary;
    }

    public BaseKey setName(String name) {
        this.name = name;
        return this;
    }

    public BaseKey setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public BaseKey setPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }

}
