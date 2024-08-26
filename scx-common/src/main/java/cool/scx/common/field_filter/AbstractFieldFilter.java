package cool.scx.common.field_filter;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.addAll;

public abstract class AbstractFieldFilter implements FieldFilter {

    /**
     * 包含的列
     */
    private final Set<String> fieldNames;

    /**
     * 当一个实体类所对应的 field 的值为 null 时, 是否将此 field 所对应的列排除 默认启用
     */
    private boolean ignoreNullValue;

    protected AbstractFieldFilter() {
        this.fieldNames = new HashSet<>();
        this.ignoreNullValue = true;
    }

    /**
     * 添加 包含类型的列
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    protected FieldFilter _addFieldNames(String... fieldNames) {
        addAll(this.fieldNames, fieldNames);
        return this;
    }

    /**
     * 根据指定名称 移除 包含类型的列
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    protected FieldFilter _removeFieldNames(String... fieldNames) {
        for (var fieldName : fieldNames) {
            this.fieldNames.remove(fieldName);
        }
        return this;
    }

    /**
     * 清除所有 包含类型的列
     *
     * @return this 方便链式调用
     */
    @Override
    public FieldFilter clear() {
        this.fieldNames.clear();
        return this;
    }

    @Override
    public String[] getFieldNames() {
        return fieldNames.toArray(String[]::new);
    }

    @Override
    public boolean getIgnoreNullValue() {
        return ignoreNullValue;
    }

    @Override
    public FieldFilter ignoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
    }

}
