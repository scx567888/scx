package cool.scx.core;

import cool.scx.dao.ScxDaoTableInfo;
import cool.scx.sql.SQLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

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
        var databaseName = ScxContext.options().dataSourceDatabase();
        var dataSource = ScxContext.dao().dataSource();
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
                if (SQLHelper.checkNeedFixTable(tableInfo, databaseName, dataSource)) {
                    SQLHelper.fixTable(tableInfo, databaseName, dataSource);
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
        return Arrays.stream(ScxContext.scxModules())
                .flatMap(c -> c.classList().stream())
                .filter(ScxHelper::isScxBaseModelClass)// 继承自 BaseModel
                .toList();
    }

    /**
     * 检查是否有任何 (BaseModel) 类需要修复表
     *
     * @return 是否有
     */
    public static boolean checkNeedFixTable() {
        logger.debug("检查数据表结构中...");
        var databaseName = ScxContext.options().dataSourceDatabase();
        var dataSource = ScxContext.dao().dataSource();
        for (var v : getAllScxBaseModelClassList()) {
            //根据 class 获取 tableInfo
            var tableInfo = new ScxDaoTableInfo(v);
            try {
                //有任何需要修复的直接 返回 true
                if (SQLHelper.checkNeedFixTable(tableInfo, databaseName, dataSource)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
