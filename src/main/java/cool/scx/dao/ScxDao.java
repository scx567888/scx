package cool.scx.dao;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;
import cool.scx.config.ScxEasyConfig;
import cool.scx.sql.SQLRunner;
import cool.scx.util.ConsoleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * <p>ScxDBContext class.</p>
 *
 * @author scx567888
 * @version 1.1.5
 */
public final class ScxDao {

    /**
     * a
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxDao.class);

    /**
     * 数据源
     */
    private final DataSource dataSource;

    /**
     * a
     */
    private final SQLRunner sqlRunner;

    /**
     * a
     *
     * @param easyConfig a
     */
    public ScxDao(ScxEasyConfig easyConfig) {
        var mysqlDataSource = getMySQLDataSource(easyConfig);
        this.dataSource = getHikariDataSource(mysqlDataSource);
        this.sqlRunner = new SQLRunner(this.dataSource);
    }

    /**
     * HikariDataSource 初始化 HikariDataSource 数据源 （此处内部使用连接池提高性能）
     *
     * @param mysqlDataSource s
     * @return s
     */
    private static DataSource getHikariDataSource(MysqlDataSource mysqlDataSource) {
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setDataSource(mysqlDataSource);
        return hikariDataSource;
    }

    /**
     * 初始化 MySQL 数据源
     *
     * @param scxEasyConfig a {@link cool.scx.config.ScxEasyConfig} object
     * @return MySQL 数据源
     */
    private static MysqlDataSource getMySQLDataSource(ScxEasyConfig scxEasyConfig) {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName(scxEasyConfig.dataSourceHost());
        mysqlDataSource.setDatabaseName(scxEasyConfig.dataSourceDatabase());
        mysqlDataSource.setUser(scxEasyConfig.dataSourceUsername());
        mysqlDataSource.setPassword(scxEasyConfig.dataSourcePassword());
        mysqlDataSource.setPort(scxEasyConfig.dataSourcePort());
        // 设置参数值
        for (var parameter : scxEasyConfig.dataSourceParameters()) {
            var p = parameter.split("=");
            if (p.length == 2) {
                var property = mysqlDataSource.getProperty(p[0]);
                property.setValue(property.getPropertyDefinition().parseObject(p[1], null));
            }
        }
        return mysqlDataSource;
    }

    /**
     * 数据源连接异常
     *
     * @param e a {@link java.lang.Exception} object.
     */
    public static void dataSourceExceptionHandler(Exception e) {
        while (true) {
            var errMessage = """
                    **************************************************************
                    *                                                            *
                    *           X 数据源连接失败 !!! 是否忽略错误并继续运行 ?            *
                    *                                                            *
                    *        [Y] 忽略错误并继续运行    |     [N] 退出程序              *
                    *                                                            *
                    **************************************************************
                    """;
            System.err.println(errMessage);
            var result = ConsoleUtils.readLine().trim();
            if ("Y".equalsIgnoreCase(result)) {
                var ignoreMessage = """
                        *******************************************
                        *                                         *
                        *       N 数据源链接错误,用户已忽略 !!!         *
                        *                                         *
                        *******************************************
                        """;
                System.err.println(ignoreMessage);
                break;
            } else if ("N".equalsIgnoreCase(result)) {
                e.printStackTrace();
                System.exit(-1);
                break;
            }
        }
    }

    /**
     * 检查数据源是否可用
     *
     * @return b
     */
    public boolean checkDataSource() {
        try (var conn = dataSource().getConnection()) {
            var dm = conn.getMetaData();
            logger.debug("数据源连接成功 : 类型 [{}]  版本 [{}]", dm.getDatabaseProductName(), dm.getDatabaseProductVersion());
            return true;
        } catch (Exception e) {
            dataSourceExceptionHandler(e);
            return false;
        }
    }

    /**
     * <p>dataSource.</p>
     *
     * @return a DataSource object
     */
    public DataSource dataSource() {
        return dataSource;
    }

    /**
     * a
     *
     * @return a
     */
    public SQLRunner sqlRunner() {
        return sqlRunner;
    }

}
