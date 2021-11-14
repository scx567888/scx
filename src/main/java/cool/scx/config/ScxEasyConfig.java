package cool.scx.config;

import cool.scx.ScxAppRoot;
import cool.scx.config.handler.impl.AppRootHandler;
import cool.scx.config.handler.impl.ConvertStaticServerHandler;
import cool.scx.config.handler.impl.DecryptValueHandler;
import cool.scx.util.NetUtils;
import cool.scx.util.ansi.Ansi;
import cool.scx.web.handler.StaticServer;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>ScxEasyConfig class.</p>
 *
 * @author scx567888
 * @version 1.3.0
 */
public final class ScxEasyConfig {

    /**
     * 端口号
     */
    private final int port;

    /**
     * 允许的 域
     */
    private final String allowedOrigin;

    /**
     * 是否真实删除
     */
    private final boolean tombstone;

    /**
     * 数据源地址
     */
    private final String dataSourceHost;

    /**
     * 数据源端口
     */
    private final Integer dataSourcePort;

    /**
     * 数据源端口
     */
    private final String dataSourceDatabase;

    /**
     * 数据源 用户名
     */
    private final String dataSourceUsername;

    /**
     * 数据源密码 真实值(解密后)
     */

    private final String dataSourcePassword;

    /**
     * 其他连接参数
     */
    private final Set<String> dataSourceParameters;

    /**
     * 是否开启 https
     */
    private final boolean httpsEnabled;

    /**
     * ssl 证书路径 字符串值
     */
    private final File sslPath;

    /**
     * ssl 证书密码 (解密后)
     */
    private final String sslPassword;

    /**
     * 模板 根目录 字符串值
     */
    private final File templateRoot;

    /**
     * 静态服务器列表
     */
    private final List<StaticServer> staticServers;

    /**
     * a
     *
     * @param scxConfig  a
     * @param scxAppRoot a
     */
    public ScxEasyConfig(ScxConfig scxConfig, ScxAppRoot scxAppRoot, String appKey) {
        port = scxConfig.getOrDefault("scx.port", 8080);
        tombstone = scxConfig.getOrDefault("scx.tombstone", false);
        allowedOrigin = scxConfig.getOrDefault("scx.allowed-origin", "*");
        templateRoot = scxConfig.get("scx.template.root", new AppRootHandler("AppRoot:/c/", scxAppRoot));
        staticServers = scxConfig.get("scx.static-servers", new ConvertStaticServerHandler(scxAppRoot));
        httpsEnabled = scxConfig.getOrDefault("scx.https.enabled", false);
        sslPath = scxConfig.get("scx.https.ssl-path", new AppRootHandler(scxAppRoot));
        sslPassword = scxConfig.get("scx.https.ssl-password", new DecryptValueHandler(appKey));
        dataSourceHost = scxConfig.getOrDefault("scx.data-source.host", "127.0.0.1");
        dataSourcePort = scxConfig.getOrDefault("scx.data-source.port", 3306);
        dataSourceDatabase = scxConfig.get("scx.data-source.database", String.class);
        dataSourceUsername = scxConfig.get("scx.data-source.username", String.class);
        dataSourcePassword = scxConfig.get("scx.data-source.password", new DecryptValueHandler(appKey));
        dataSourceParameters = scxConfig.getOrDefault("scx.data-source.parameters", new HashSet<>());
    }

    /**
     * <p>dataSourceHost.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String dataSourceHost() {
        return dataSourceHost;
    }

    /**
     * <p>dataSourcePort.</p>
     *
     * @return a int.
     */
    public int dataSourcePort() {
        return dataSourcePort;
    }

    /**
     * <p>dataSourceDatabase.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String dataSourceDatabase() {
        return dataSourceDatabase;
    }

    /**
     * <p>dataSourceParameters.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> dataSourceParameters() {
        return dataSourceParameters;
    }

    /**
     * <p>dataSourceUsername.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String dataSourceUsername() {
        return dataSourceUsername;
    }

    /**
     * <p>dataSourcePassword.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String dataSourcePassword() {
        return dataSourcePassword;
    }

    /**
     * <p>isHttpsEnabled.</p>
     *
     * @return a boolean.
     */
    public boolean isHttpsEnabled() {
        return httpsEnabled;
    }

    /**
     * <p>port.</p>
     *
     * @return a int.
     */
    public int port() {
        return port;
    }

    /**
     * <p>sslPath.</p>
     *
     * @return a {@link java.io.File} object.
     */
    public File sslPath() {
        return sslPath;
    }

    /**
     * <p>sslPassword.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String sslPassword() {
        return sslPassword;
    }

    /**
     * <p>realDelete.</p>
     *
     * @return a boolean.
     */
    public boolean tombstone() {
        return tombstone;
    }

    /**
     * 获取模板根路径
     *
     * @return a {@link java.io.File} object.
     */
    public File templateRoot() {
        return templateRoot;
    }

    /**
     * <p>staticServers.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StaticServer> staticServers() {
        return staticServers;
    }

    /**
     * <p>allowedOrigin.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String allowedOrigin() {
        return allowedOrigin;
    }

    /**
     * 打印 easyConfig 的信息
     */
    public void showEasyConfigInfo() {
        Ansi.out()
                .green("Y 服务器 IP 地址                       \t -->\t " + NetUtils.getLocalAddress()).ln()
                .green("Y 端口号                               \t -->\t " + port).ln()
                .green("Y 数据库删除方式为                     \t -->\t " + (tombstone ? "逻辑删除" : "物理删除")).ln()
                .green("Y 允许的请求源                         \t -->\t " + allowedOrigin).ln()
                .green("Y 模板根目录                           \t -->\t " + templateRoot.getPath()).ln()
                .green("Y 静态资源服务器                       \t -->\t " + staticServers.stream().map(StaticServer::location).collect(Collectors.joining(", ", "[", "]"))).ln()
                .green("Y 是否开启 https                       \t -->\t " + (httpsEnabled ? "是" : "否")).ln()
                .green("Y 证书路径                            \t -->\t " + (sslPath != null ? sslPath.getPath() : "")).ln()
                .green("Y 证书密码                            \t -->\t *****").ln()
                .green("Y 数据源 Host                          \t -->\t " + dataSourceHost).ln()
                .green("Y 数据源 端口号                        \t -->\t " + port).ln()
                .green("Y 数据源 数据库名称                    \t -->\t " + dataSourceDatabase).ln()
                .green("Y 数据源 用户名                        \t -->\t " + dataSourceUsername).ln()
                .green("Y 数据源 连接密码                      \t -->\t *****").ln()
                .green("Y 数据源 连接参数                      \t -->\t " + dataSourceParameters.toString()).println();
    }

}
