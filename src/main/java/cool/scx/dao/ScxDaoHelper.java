package cool.scx.dao;

import cool.scx.ScxContext;
import cool.scx.sql.SQLRunner;
import cool.scx.util.CaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
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
        for (var v : getAllScxModelClassList()) {
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
    private static List<Class<?>> getAllScxModelClassList() {
        var allScxModelClassList = new ArrayList<Class<?>>();
        for (var scxModuleInfo : ScxContext.scxModuleInfos()) {
            //只对 ScxModel 注解标识的了类进行数据表修复
            allScxModelClassList.addAll(scxModuleInfo.scxModelClassList());
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
        for (var v : getAllScxModelClassList()) {
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
     * @throws SQLException a
     */
    private static void fixTable0(ScxDaoTableInfo tableInfo) throws SQLException {
        var databaseName = ScxContext.easyConfig().dataSourceDatabase();
        try (var con = ScxContext.dao().dataSource().getConnection()) {
            var dbMetaData = con.getMetaData();
            var nowTable = dbMetaData.getTables(databaseName, databaseName, tableInfo.tableName, new String[]{"TABLE"});
            if (nowTable.next()) { //有这个表
                var nowColumns = dbMetaData.getColumns(databaseName, databaseName, nowTable.getString("TABLE_NAME"), null);
                //所有不包含的 field
                var nonExistentFields = getNonExistentColumnName(nowColumns, tableInfo);
                if (nonExistentFields.size() > 0) {
                    var alertTableDDL = tableInfo.getAlertTableDDL(nonExistentFields);
                    SQLRunner.execute(con, alertTableDDL);
                }
            } else {//没有这个表
                SQLRunner.execute(con, tableInfo.getCreateTableDDL());
            }
        }
    }

    /**
     * 检查是否需要修复表
     *
     * @param tableInfo a
     * @return true 需要 false 不需要
     * @throws SQLException e
     */
    private static boolean checkNeedFixTable0(ScxDaoTableInfo tableInfo) throws SQLException {
        var databaseName = ScxContext.easyConfig().dataSourceDatabase();
        try (var con = ScxContext.dao().dataSource().getConnection()) {
            var dbMetaData = con.getMetaData();
            var nowTable = dbMetaData.getTables(databaseName, databaseName, tableInfo.tableName, new String[]{"TABLE"});
            if (nowTable.next()) { //有这个表
                var nowColumns = dbMetaData.getColumns(databaseName, databaseName, nowTable.getString("TABLE_NAME"), null);
                //所有不包含的 field
                return getNonExistentColumnName(nowColumns, tableInfo).size() != 0;
            } else {
                return true;
            }
        }
    }

    /**
     * 获取不存在的 列名称 用于后续的表修复
     *
     * @param nowColumns n
     * @param tableInfo  a
     * @return a
     * @throws SQLException a
     */
    private static List<String> getNonExistentColumnName(ResultSet nowColumns, ScxDaoTableInfo tableInfo) throws SQLException {
        var existingColumn = new ArrayList<>();
        while (nowColumns.next()) {
            existingColumn.add(nowColumns.getString("COLUMN_NAME"));
        }
        //所有不包含的
        return Stream.of(tableInfo.allFields)
                .map(Field::getName)
                .filter(name -> !existingColumn.contains(CaseUtils.toSnake(name)))
                .toList();
    }

}
