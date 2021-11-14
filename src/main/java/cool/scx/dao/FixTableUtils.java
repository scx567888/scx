package cool.scx.dao;

import cool.scx.ScxContext;
import cool.scx.util.ConsoleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>SQLDDLHelper class.</p>
 *
 * @author scx567888
 * @version 1.4.4
 */
public final class FixTableUtils {

    private static final Logger logger = LoggerFactory.getLogger(FixTableUtils.class);

    /**
     * <p>fixTable.</p>
     *
     * @param ignoreConfirm 是否显示提示框
     */
    public static void fixAllTable(boolean ignoreConfirm) {
        //如果无法链接数据库 就跳过修复表
        if (!ScxContext.dao().checkDataSource()) {
            return;
        }
        logger.debug("检查数据表结构中...");
        //已经显示过修复表的 gui 这里使用 flag 只显示一次
        boolean alreadyShowConfirmFixTable = ignoreConfirm;
        //修复成功的表
        var fixSuccess = 0;
        //修复失败的表
        var fixFail = 0;
        //不需要修复的表
        var noNeedToFix = 0;
        for (var v : getAllScxModelClassList()) {
            //根据 class 获取 tableInfo
            var tableInfo = new TableInfo(v);
            try {
                if (tableInfo.checkNeedFixTable()) {
                    //如果已经显示过gui选择界面了就不再显示
                    if (!alreadyShowConfirmFixTable) {
                        //获取用户数据 true 为修复 false 为不修复
                        var result = confirmFixTable();
                        //如果取消修复 直接跳出这个方法
                        if (!result) {
                            logger.warn("已取消修复表...");
                            return;
                        }
                        //设置 flag
                        alreadyShowConfirmFixTable = true;
                    }
                    tableInfo.fixTable();
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
     * 向用户确认是否修复数据表
     *
     * @return a 结果
     */
    public static boolean confirmFixTable() {
        while (true) {
            System.err.println("Y 检测到需要修复数据表 , 是否修复? [Y]修复数据表 [N]忽略 [Q]退出 ");
            var result = ConsoleUtils.readLine().trim();
            if ("Y".equalsIgnoreCase(result)) {
                return true;
            } else if ("N".equalsIgnoreCase(result)) {
                return false;
            } else if ("Q".equalsIgnoreCase(result)) {
                System.exit(-1);
                return false;
            }
        }
    }

}
