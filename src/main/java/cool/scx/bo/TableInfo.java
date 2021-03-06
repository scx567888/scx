package cool.scx.bo;

import cool.scx.annotation.Column;
import cool.scx.annotation.NoColumn;
import cool.scx.annotation.ScxModel;
import cool.scx.config.ScxConfig;
import cool.scx.util.CaseUtils;
import cool.scx.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 用于描述根据 class 构建的对应的数据表结构
 *
 * @author scx567888
 * @version 0.5.0
 */
public final class TableInfo {

    /**
     * 实体类型 不含@NoColunm 和@NoUpdate 注解的 field
     */
    public final Field[] canUpdateFields;

    /**
     * 实体类型 不含@NoColunm 和@NoInsert 注解的 field
     */
    public final Field[] canInsertFields;

    /**
     * 实体类型不含@NoColunm 注解的field
     */
    public final Field[] allFields;

    /**
     * 表名
     */
    public final String tableName;

    /**
     * 所有select sql的列名，有带下划线的将其转为aa_bb AS aaBb
     */
    public final String[] selectColumns;

    /**
     * c
     *
     * @param clazz a {@link java.lang.Class} object.
     */
    public TableInfo(Class<?> clazz) {
        tableName = getTableName(clazz);
        allFields = getAllFields(clazz);
        canInsertFields = getCanInsertFields(allFields);
        canUpdateFields = getCanUpdateFields(allFields);
        selectColumns = getSelectColumns(allFields);
    }

    private static Field[] getAllFields(Class<?> clazz) {
        return Stream.of(clazz.getFields()).filter(field -> !field.isAnnotationPresent(NoColumn.class)).toArray(Field[]::new);
    }

    private static Field[] getCanInsertFields(Field[] allFields) {
        return Arrays.stream(allFields).filter(ta -> {
            var column = ta.getAnnotation(Column.class);
            return column == null || !column.excludeOnInsert();
        }).toArray(Field[]::new);
    }

    private static Field[] getCanUpdateFields(Field[] allFields) {
        return Arrays.stream(allFields).filter(ta -> {
            var column = ta.getAnnotation(Column.class);
            return column == null || !column.excludeOnUpdate();
        }).toArray(Field[]::new);
    }

    private static String[] getSelectColumns(Field[] allFields) {
        return Stream.of(allFields).filter(field -> ScxConfig.realDelete() || !"tombstone".equals(field.getName())).map(field -> {
            var underscore = CaseUtils.toSnake(field.getName());
            return underscore.contains("_") ? underscore + " AS " + field.getName() : underscore;
        }).toArray(String[]::new);
    }

    private static String getTableName(Class<?> clazz) {
        var scxModel = clazz.getAnnotation(ScxModel.class);
        if (scxModel != null && StringUtils.isNotEmpty(scxModel.tableName())) {
            return scxModel.tableName();
        }
        if (scxModel != null && StringUtils.isNotEmpty(scxModel.tablePrefix())) {
            return scxModel.tablePrefix() + "_" + CaseUtils.toSnake(clazz.getSimpleName());
        }
        return "scx_" + CaseUtils.toSnake(clazz.getSimpleName());
    }

}
