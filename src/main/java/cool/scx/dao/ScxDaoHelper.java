package cool.scx.dao;

import cool.scx.ScxContext;
import cool.scx.sql.SQLRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>SQLDDLHelper class.</p>
 *
 * @author scx567888
 * @version 1.4.4
 */
public final class ScxDaoHelper {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxDaoHelper.class);

    /**
     * <p>fixTable.</p>
     */
    public static void fixTable() {
        logger.debug("修复数据表结构中...");
        //修复成功的表
        var fixSuccess = 0;
        //修复失败的表
        var fixFail = 0;
        //不需要修复的表
        var noNeedToFix = 0;
        for (var v : getAllScxBaseModelClassList()) {
            //根据 class 获取 tableInfo
            var tableInfo = new ScxDaoTableInfo(v);
            try {
                if (checkNeedFixTable0(tableInfo)) {
                    fixTable0(tableInfo);
                    fixSuccess = fixSuccess + 1;
                } else {
                    noNeedToFix = noNeedToFix + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                fixFail = fixFail + 1;
            }
        }

        if (fixSuccess != 0) {
            logger.debug("修复成功 {} 张表...", fixSuccess);
        }
        if (fixFail != 0) {
            logger.warn("修复失败 {} 张表...", fixFail);
        }
        if (fixSuccess + fixFail == 0) {
            logger.debug("没有表需要修复...");
        }

    }

    /**
     * 获取所有 class
     *
     * @return s
     */
    private static List<Class<?>> getAllScxBaseModelClassList() {
        var allScxModelClassList = new ArrayList<Class<?>>();
        for (var m : ScxContext.scxModuleMetadataList()) {
            //只对 ScxModel 注解标识的了类进行数据表修复
            allScxModelClassList.addAll(m.scxBaseModelClassList());
        }
        return allScxModelClassList;
    }

    /**
     * 检查是否有任何 (BaseModel) 类需要修复表
     *
     * @return 是否有
     */
    public static boolean checkNeedFixTable() {
        logger.debug("检查数据表结构中...");
        for (var v : getAllScxBaseModelClassList()) {
            //根据 class 获取 tableInfo
            var tableInfo = new ScxDaoTableInfo(v);
            try {
                //有任何需要修复的直接 返回 true
                if (checkNeedFixTable0(tableInfo)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * a
     *
     * @param tableInfo a
     * @throws java.sql.SQLException a
     */
    private static void fixTable0(ScxDaoTableInfo tableInfo) throws SQLException {
        var databaseName = ScxContext.easyConfig().dataSourceDatabase();
        try (var con = ScxContext.dao().dataSource().getConnection()) {
            var existingColumn = getTableAllColumnNames(con, databaseName, tableInfo.tableName());
            if (existingColumn != null) {
                //获取不存在的字段
                var nonExistentColumnNames = Stream.of(tableInfo.columnInfos()).filter(c -> !existingColumn.contains(c.columnName())).toList();
                if (nonExistentColumnNames.size() > 0) {
                    var alertTableDDL = tableInfo.getAlertTableDDL(nonExistentColumnNames);
                    SQLRunner.execute(con, alertTableDDL);
                }
            } else {// 没有这个表
                SQLRunner.execute(con, tableInfo.getCreateTableDDL());
            }
        }
    }

    /**
     * 检查是否需要修复表
     *
     * @param tableInfo a
     * @return true 需要 false 不需要
     * @throws java.sql.SQLException e
     */
    private static boolean checkNeedFixTable0(ScxDaoTableInfo tableInfo) throws SQLException {
        var databaseName = ScxContext.easyConfig().dataSourceDatabase();
        try (var con = ScxContext.dao().dataSource().getConnection()) {
            var existingColumn = getTableAllColumnNames(con, databaseName, tableInfo.tableName());
            //这个表不存在
            if (existingColumn != null) {
                //获取不存在的字段
                var nonExistentColumnNames = Stream.of(tableInfo.columnInfos()).filter(c -> !existingColumn.contains(c.columnName())).toList();
                return nonExistentColumnNames.size() != 0;
            } else {
                return true;
            }
        }
    }

    /**
     * 根据连接 获取数据库中所有的字段
     *
     * @param con          连接
     * @param databaseName 数据库名称
     * @param tableName    表名称
     * @return 如果表存在返回所有字段的名称 否则返回 null
     * @throws java.sql.SQLException s
     */
    public static List<String> getTableAllColumnNames(Connection con, String databaseName, String tableName) throws SQLException {
        var dbMetaData = con.getMetaData();
        var nowTable = dbMetaData.getTables(databaseName, databaseName, tableName, new String[]{"TABLE"});
        if (nowTable.next()) { //有这个表
            var nowColumns = dbMetaData.getColumns(databaseName, databaseName, nowTable.getString("TABLE_NAME"), null);
            var existingColumn = new ArrayList<String>();
            while (nowColumns.next()) {
                existingColumn.add(nowColumns.getString("COLUMN_NAME"));
            }
            return existingColumn;
        } else {//没有这个表
            return null;
        }
    }

}
