package cool.scx.dao;

import cool.scx.ScxContext;
import cool.scx.annotation.Column;
import cool.scx.annotation.NoColumn;
import cool.scx.annotation.ScxModel;
import cool.scx.sql.SQLRunner;
import cool.scx.sql.SQLTypeHelper;
import cool.scx.util.CaseUtils;
import cool.scx.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public ScxDaoTableInfo(Class<?> clazz) {
        this.tableName = initTableName(clazz);
        this.allFields = initAllFields(clazz);
        this.canInsertFields = initCanInsertFields(allFields);
        this.canUpdateFields = initCanUpdateFields(allFields);
        this.selectColumns = initSelectColumns(allFields);
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    private static Field[] initAllFields(Class<?> clazz) {
        return Stream.of(clazz.getFields()).filter(field -> !field.isAnnotationPresent(NoColumn.class)).toArray(Field[]::new);
    }

    /**
     * a
     *
     * @param allFields a
     * @return a
     */
    private static Field[] initCanInsertFields(Field[] allFields) {
        return Arrays.stream(allFields).filter(ta -> {
            var column = ta.getAnnotation(Column.class);
            return column == null || !column.excludeOnInsert();
        }).toArray(Field[]::new);
    }

    /**
     * a
     *
     * @param allFields a
     * @return a
     */
    private static Field[] initCanUpdateFields(Field[] allFields) {
        return Arrays.stream(allFields).filter(ta -> {
            var column = ta.getAnnotation(Column.class);
            return column == null || !column.excludeOnUpdate();
        }).toArray(Field[]::new);
    }

    /**
     * a
     *
     * @param allFields a
     * @return a
     */
    private static String[] initSelectColumns(Field[] allFields) {
        return Stream.of(allFields).filter(field -> !ScxContext.easyConfig().tombstone() || !"tombstone".equals(field.getName())).map(field -> {
            var underscore = CaseUtils.toSnake(field.getName());
            return underscore.contains("_") ? underscore + " AS " + field.getName() : underscore;
        }).toArray(String[]::new);
    }

    /**
     * a
     *
     * @param clazz a
     * @return a
     */
    private static String initTableName(Class<?> clazz) {
        var scxModel = clazz.getAnnotation(ScxModel.class);
        if (scxModel != null && StringUtils.isNotBlank(scxModel.tableName())) {
            return scxModel.tableName();
        }
        if (scxModel != null && StringUtils.isNotBlank(scxModel.tablePrefix())) {
            return scxModel.tablePrefix() + "_" + CaseUtils.toSnake(clazz.getSimpleName());
        }
        //这里判断一下是否使用了数据库 如果使用 则表名省略掉 数据库限定名 否则的话则添加数据库限定名
        return "scx_" + CaseUtils.toSnake(clazz.getSimpleName());
    }

    /**
     * 根据 field 构建 特殊的 SQLColumn
     * 如 是否为唯一键 是否添加索引 是否为主键等
     *
     * @param allFields allFields
     * @return 生成的语句片段
     */
    private static List<String> getOtherSQL(Field... allFields) {
        var list = new ArrayList<String>();
        for (Field field : allFields) {
            var column = field.getAnnotation(Column.class);
            if (column != null) {
                var columnName = CaseUtils.toSnake(field.getName());
                if (column.primaryKey()) {
                    list.add("PRIMARY KEY (`" + columnName + "`)");
                }
                if (column.unique()) {
                    list.add("UNIQUE KEY `unique_" + columnName + "`(`" + columnName + "`)");
                }
                if (column.needIndex()) {
                    list.add("KEY `index_" + columnName + "`(`" + columnName + "`)");
                }
            }
        }
        return list;
    }

    /**
     * 根据 field 构建 基本的 SQLColumn
     *
     * @param field field
     * @return 生成的语句片段
     */
    private static String getMySQLColumnByField(Field field) {
        var columnName = "`" + CaseUtils.toSnake(field.getName()) + "` ";
        var type = "";
        var notNull = "";
        var autoIncrement = "";
        var defaultValue = "";
        var onUpdate = "";
        var fieldColumn = field.getAnnotation(Column.class);
        if (fieldColumn != null) {
            type = "".equals(fieldColumn.type()) ? SQLTypeHelper.getMySQLTypeCreateName(field.getType()) : fieldColumn.type();
            notNull = fieldColumn.notNull() ? " NOT NULL" : " NULL";
            if (fieldColumn.autoIncrement()) {
                autoIncrement = " AUTO_INCREMENT ";
            }
            if (fieldColumn.primaryKey()) {
                notNull = " NOT NULL ";
            }
            if (!"".equals(fieldColumn.defaultValue())) {
                defaultValue = " DEFAULT " + fieldColumn.defaultValue();
            }
            if (!"".equals(fieldColumn.onUpdateValue())) {
                onUpdate += " ON UPDATE " + fieldColumn.defaultValue();
            }
        } else {
            type = SQLTypeHelper.getMySQLTypeCreateName(field.getType());
            notNull = " NULL ";
        }
        return columnName + type + notNull + autoIncrement + defaultValue + onUpdate;
    }

    private static Field[] getNonExistentFields(ResultSet nowColumns, Field[] allFields) throws SQLException {
        var existingColumn = new ArrayList<>();
        while (nowColumns.next()) {
            existingColumn.add(nowColumns.getString("COLUMN_NAME"));
        }
        //所有不包含的
        return Stream.of(allFields).filter(field -> !existingColumn.contains(CaseUtils.toSnake(field.getName()))).toArray(Field[]::new);
    }

    /**
     * 获取建表语句
     *
     * @return s
     */
    public String getCreateTableDDL() {
        var createTableDDL = new ArrayList<String>();
        for (var field : allFields) {
            createTableDDL.add(getMySQLColumnByField(field));
        }
        createTableDDL.addAll(getOtherSQL(allFields));
        return "CREATE TABLE `" + tableName + "` (" + String.join(",", createTableDDL) + ");";
    }

    /**
     * 获取修复表的语句
     *
     * @param nonExistentFields a
     * @return a
     */
    public String getAlertTableDDL(Field[] nonExistentFields) {
        var alertTableDDL = new ArrayList<String>();
        for (var field : nonExistentFields) {
            alertTableDDL.add("ADD " + getMySQLColumnByField(field));
        }
        for (var s : getOtherSQL(nonExistentFields)) {
            alertTableDDL.add("ADD " + s);
        }
        return "ALTER TABLE `" + tableName + "` " + String.join(",", alertTableDDL) + ";";
    }

    /**
     * @throws SQLException a
     */
    public void fixTable() throws SQLException {
        var databaseName = ScxContext.easyConfig().dataSourceDatabase();
        try (var con = ScxContext.dao().dataSource().getConnection()) {
            var dbMetaData = con.getMetaData();
            var nowTable = dbMetaData.getTables(databaseName, databaseName, tableName, new String[]{"TABLE"});
            if (nowTable.next()) { //有这个表
                var nowColumns = dbMetaData.getColumns(databaseName, databaseName, nowTable.getString("TABLE_NAME"), null);
                //所有不包含的 field
                var nonExistentFields = getNonExistentFields(nowColumns, allFields);
                if (nonExistentFields.length > 0) {
                    var alertTableDDL = this.getAlertTableDDL(nonExistentFields);
                    SQLRunner.execute(con, alertTableDDL);
                }
            } else {//没有这个表
                SQLRunner.execute(con, this.getCreateTableDDL());
            }
        }

    }

    /**
     * 检查是否需要修复表
     *
     * @return true 需要 false 不需要
     * @throws SQLException e
     */
    public boolean checkNeedFixTable() throws SQLException {
        var databaseName = ScxContext.easyConfig().dataSourceDatabase();
        try (var con = ScxContext.dao().dataSource().getConnection()) {
            var dbMetaData = con.getMetaData();
            var nowTable = dbMetaData.getTables(databaseName, databaseName, tableName, new String[]{"TABLE"});
            if (nowTable.next()) { //有这个表
                var nowColumns = dbMetaData.getColumns(databaseName, databaseName, nowTable.getString("TABLE_NAME"), null);
                //所有不包含的 field
                var nonExistentFieldLength = getNonExistentFields(nowColumns, allFields).length;
                return nonExistentFieldLength != 0;
            } else {
                return true;
            }
        }
    }

}