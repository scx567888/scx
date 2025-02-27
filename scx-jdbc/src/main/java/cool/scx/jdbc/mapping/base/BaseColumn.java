package cool.scx.jdbc.mapping.base;

import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.DataType;

/// 用于手动编写 Column
///
/// @author scx567888
/// @version 0.0.1
public class BaseColumn implements Column {

    private String table;
    private String name;
    private BaseDataType dataType;
    private String defaultValue;
    private String onUpdate;
    private boolean notNull;
    private boolean autoIncrement;
    private boolean primary;
    private boolean unique;
    private boolean index;
    private String comment;

    public BaseColumn() {

    }

    public BaseColumn(Column oldColumn) {
        setTable(oldColumn.table());
        setName(oldColumn.name());
        setDataType(oldColumn.dataType());
        setDefaultValue(oldColumn.defaultValue());
        setOnUpdate(oldColumn.onUpdate());
        setNotNull(oldColumn.notNull());
        setAutoIncrement(oldColumn.autoIncrement());
        setPrimary(oldColumn.primary());
        setUnique(oldColumn.unique());
        setIndex(oldColumn.index());
        setComment(oldColumn.comment());
    }

    @Override
    public String table() {
        return table;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public BaseDataType dataType() {
        return dataType;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String onUpdate() {
        return onUpdate;
    }

    @Override
    public boolean notNull() {
        return notNull;
    }

    @Override
    public boolean autoIncrement() {
        return autoIncrement;
    }

    @Override
    public boolean primary() {
        return primary;
    }

    @Override
    public boolean unique() {
        return unique;
    }

    @Override
    public boolean index() {
        return index;
    }

    @Override
    public String comment() {
        return comment;
    }

    public BaseColumn setTable(String table) {
        this.table = table;
        return this;
    }

    public BaseColumn setName(String name) {
        this.name = name;
        return this;
    }

    public BaseColumn setDataType(DataType dataType) {
        this.dataType = new BaseDataType(dataType);
        return this;
    }

    public BaseColumn setDataType(String name) {
        this.dataType = new BaseDataType(name);
        return this;
    }

    public BaseColumn setDataType(String name, Integer length) {
        this.dataType = new BaseDataType(name, length);
        return this;
    }

    public BaseColumn setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public BaseColumn setOnUpdate(String onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    public BaseColumn setNotNull(boolean notNull) {
        this.notNull = notNull;
        return this;
    }

    public BaseColumn setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
        return this;
    }

    public BaseColumn setPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }

    public BaseColumn setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    public BaseColumn setIndex(boolean index) {
        this.index = index;
        return this;
    }

    public BaseColumn setComment(String comment) {
        this.comment = comment;
        return this;
    }

}
