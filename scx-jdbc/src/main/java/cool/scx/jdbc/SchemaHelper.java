package cool.scx.jdbc;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;

import java.sql.SQLException;
import java.util.ArrayList;

import static cool.scx.jdbc.meta_data.MetaDataHelper.getCurrentSchema;
import static cool.scx.jdbc.sql.SQL.sql;

/// 架构管理工具
///
/// @author scx567888
/// @version 0.0.1
public final class SchemaHelper {

    /// 获取迁移 SQL  (当前只支持 创建不存在的新列)
    /// todo 需要支持删除旧列和修改列
    ///
    /// @param oldTable 旧表
    /// @param newTable 新表
    /// @param dialect  方言
    /// @return sql 不需要迁移语句则返回 null
    public static String getMigrateSQL(Table oldTable, Table newTable, Dialect dialect) {
        var verifyResult = verifyTable(oldTable, newTable);
        if (verifyResult.notEmpty()) {
            //默认不去 drop 任何字段 太危险了
            return dialect.getAlterTableDDL(verifyResult.needAdd(), new Column[]{}, verifyResult.needChange(), newTable);
        }
        return null;
    }

    public static ColumnVerifyResult verifyColumn(Column oldColumn, Column newColumn) {
        var needChangeDataType = false;
        var needChangeIndex = false;
        var needChangeDefaultValue = false;
        //1, 类型不相同
        if (oldColumn.dataType().jdbcType() != newColumn.dataType().jdbcType()) {
            needChangeDataType = true;
        }
        //2, 只处理原来没有索引但是 现在有索引的情况
        if (!oldColumn.index() && newColumn.index()) {
            needChangeIndex = true;
        }
        //3, 处理默认值 只处理 原来没有默认值的 后加的 
        // 原有默认值是 null
        if (oldColumn.defaultValue() == null) {
            // 新的必须也是 null
            if (newColumn.defaultValue() != null) {
                needChangeDefaultValue = true;
            }
        }

        return new ColumnVerifyResult(needChangeDataType, needChangeIndex, needChangeDefaultValue);
    }

    public static TableVerifyResult verifyTable(Table oldTable, Table newTable) {
        var needAdd = new ArrayList<Column>();
        var needRemove = new ArrayList<Column>();
        var needChange = new ArrayList<NeedChangeColumn>();
        for (var oldColumn : oldTable.columns()) {
            var newColumn = newTable.getColumn(oldColumn.name());
            if (newColumn == null) {
                needRemove.add(oldColumn);
            } else {
                var columnVerifyResult = verifyColumn(oldColumn, newColumn);
                if (columnVerifyResult.notEmpty()) {
                    needChange.add(new NeedChangeColumn(oldColumn, newColumn, columnVerifyResult));
                }
            }
        }
        for (var newColumn : newTable.columns()) {
            var oldColumn = oldTable.getColumn(newColumn.name());
            if (oldColumn == null) {
                needAdd.add(newColumn);
            }
        }
        return new TableVerifyResult(
                needAdd.toArray(Column[]::new),
                needRemove.toArray(Column[]::new),
                needChange.toArray(NeedChangeColumn[]::new)
        );
    }

    /// 和当前数据中同名表进行比对 进行修复  (若表不存在则创建,若存在则只添加不存在的列)
    ///
    /// @param tableInfo a
    /// @throws java.sql.SQLException a
    public static void fixTable(Table tableInfo, JDBCContext jdbcContext) throws SQLException {
        try (var con = jdbcContext.dataSource().getConnection()) {
            // 查找同名表
            var tableMetaData = getCurrentSchema(con).getTable(con, tableInfo.name());

            // 没有这个表 创建表
            if (tableMetaData == null) {
                var createTableDDL = jdbcContext.dialect().getCreateTableDDL(tableInfo);
                jdbcContext.sqlRunner().execute(con, sql(createTableDDL));
                return;
            }

            // 有表 获取迁移 SQL
            var migrateSQL = getMigrateSQL(tableMetaData.refreshColumns(con, jdbcContext.dialect()), tableInfo, jdbcContext.dialect());

            if (migrateSQL != null) {
                jdbcContext.sqlRunner().execute(con, sql(migrateSQL));
            }

        }
    }

    /// 检查是否需要修复表
    ///
    /// @param tableInfo a
    /// @return true 需要 false 不需要
    /// @throws java.sql.SQLException e
    public static boolean checkNeedFixTable(Table tableInfo, JDBCContext jdbcContext) throws SQLException {
        try (var con = jdbcContext.dataSource().getConnection()) {
            // 查找同名表
            var tableMetaData = getCurrentSchema(con).getTable(con, tableInfo.name());
            // 没有这个表 
            if (tableMetaData == null) {
                return true;
            }
            // 验证所需字段不为空
            var tableVerifyResult = verifyTable(tableMetaData.refreshColumns(con, jdbcContext.dialect()), tableInfo);
            return tableVerifyResult.notEmpty();
        }
    }

    public record TableVerifyResult(Column[] needAdd, Column[] needRemove, NeedChangeColumn[] needChange) {

        public boolean notEmpty() {
            return needAdd.length > 0 || needRemove.length > 0 || needChange.length > 0;
        }

    }

    public record ColumnVerifyResult(boolean needChangeDataType, boolean needChangeIndex,
                                     boolean needChangeDefaultValue) {

        public boolean notEmpty() {
            return needChangeDataType || needChangeIndex || needChangeDefaultValue;
        }

    }

    public record NeedChangeColumn(Column oldColumn, Column newColumn, ColumnVerifyResult verifyResult) {

    }

}
