package cool.scx.dao;

import cool.scx.annotation.NoColumn;
import cool.scx.annotation.ScxModel;
import cool.scx.util.CaseUtils;
import cool.scx.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 用于描述根据 class 构建的对应的数据表结构
 *
 * @author scx567888
 * @version 0.5.0
 */
public final class ScxDaoTableInfo {

    /**
     * 实体类型不含@NoColunm 注解的field
     */
    private final ScxDaoColumnInfo[] columnInfos;

    /**
     * 表名
     */
    private final String tableName;

    /**
     * c
     *
     * @param clazz a {@link java.lang.Class} object.
     */
    public ScxDaoTableInfo(Class<?> clazz) {
        this.tableName = initTableName(clazz);
        this.columnInfos = initAllColumnInfos(clazz);
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    private static ScxDaoColumnInfo[] initAllColumnInfos(Class<?> clazz) {
        return Stream.of(clazz.getFields()).filter(field -> !field.isAnnotationPresent(NoColumn.class)).map(ScxDaoColumnInfo::new).toArray(ScxDaoColumnInfo[]::new);
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    private static String initTableName(Class<?> clazz) {
        var scxModel = clazz.getAnnotation(ScxModel.class);
        if (scxModel != null && StringUtils.notBlank(scxModel.tableName())) {
            return scxModel.tableName();
        }
        if (scxModel != null && StringUtils.notBlank(scxModel.tablePrefix())) {
            return scxModel.tablePrefix() + "_" + CaseUtils.toSnake(clazz.getSimpleName());
        }
        //这里判断一下是否使用了数据库 如果使用 则表名省略掉 数据库限定名 否则的话则添加数据库限定名
        return "scx_" + CaseUtils.toSnake(clazz.getSimpleName());
    }

    /**
     * 获取建表语句
     *
     * @return s
     */
    public String getCreateTableDDL() {
        var createTableDDL = new ArrayList<String>();
        for (var columnInfo : columnInfos) {
            createTableDDL.add(columnInfo.normalDDL());
        }
        for (var columnInfo : columnInfos) {
            createTableDDL.addAll(List.of(columnInfo.specialDDL()));
        }
        return "CREATE TABLE `" + tableName + "` (" + String.join(", ", createTableDDL) + ");";
    }

    /**
     * 获取修复表的语句
     *
     * @param nonExistentColumnName java 字段的名称 (注意 : fieldNames 中存在但 allFields 中不存在的则会忽略)
     * @return a
     */
    public String getAlertTableDDL(List<ScxDaoColumnInfo> nonExistentColumnName) {
        var alertTableDDL = new ArrayList<String>();
        for (var field : nonExistentColumnName) {
            alertTableDDL.add("ADD " + field.normalDDL());
        }
        for (var s : nonExistentColumnName) {
            for (var s1 : s.specialDDL()) {
                alertTableDDL.add("ADD " + s1);
            }
        }
        return "ALTER TABLE `" + tableName + "` " + String.join(", ", alertTableDDL) + ";";
    }

    /**
     * a
     *
     * @return a
     */
    public String tableName() {
        return tableName;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxDaoColumnInfo[] columnInfos() {
        return columnInfos;
    }

}