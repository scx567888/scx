package cool.scx.app;

import cool.scx.ansi.Ansi;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.handler.AppRootHandler;
import cool.scx.config.handler.DecryptValueHandler;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import static cool.scx.common.util.NetUtils.getLocalIPAddress;
import static cool.scx.common.exception.ScxExceptionHelper.ignore;

/**
 * <p>ScxCoreConfig class.</p>
 *
 * @author scx567888
 * @version 1.3.0
 */
public final class ScxOptions {

    /**
     * 端口号
     */
    private final int port;

    /**
     * 允许的 域
     */
    private final String allowedOrigin;

    /**
     * 数据源地址
     */
    private final String dataSourceUrl;

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
    private final String[] dataSourceParameters;

    /**
     * 是否开启 https
     */
    private final boolean httpsEnabled;

    /**
     * ssl 证书路径 字符串值
     */
    private final Path sslPath;

    /**
     * ssl 证书密码 (解密后)
     */
    private final String sslPassword;

    /**
     * 模板 根目录 字符串值
     */
    private final Path templateRoot;

    /**
     * a
     *
     * @param scxConfig      a
     * @param scxEnvironment a
     * @param appKey         a
     */
    public ScxOptions(ScxConfig scxConfig, ScxEnvironment scxEnvironment, String appKey) {
        port = scxConfig.getOrDefault("scx.port", 8080);
        allowedOrigin = scxConfig.getOrDefault("scx.allowed-origin", "*");
        templateRoot = scxConfig.get("scx.template.root", AppRootHandler.of(scxEnvironment, "AppRoot:/c/"));
        httpsEnabled = scxConfig.getOrDefault("scx.https.enabled", false);
        sslPath = scxConfig.get("scx.https.ssl-path", AppRootHandler.of(scxEnvironment));
        sslPassword = scxConfig.get("scx.https.ssl-password", DecryptValueHandler.of(appKey));
        dataSourceUrl = scxConfig.getOrDefault("scx.data-source.url", "");
        dataSourceUsername = scxConfig.get("scx.data-source.username", String.class);
        dataSourcePassword = scxConfig.get("scx.data-source.password", DecryptValueHandler.of(appKey));
        dataSourceParameters = scxConfig.getOrDefault("scx.data-source.parameters", new String[]{});
    }

    public String dataSourceUrl() {
        return dataSourceUrl;
    }

    /**
     * <p>dataSourceParameters.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public String[] dataSourceParameters() {
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
    public Path sslPath() {
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
     * 获取模板根路径
     *
     * @return a {@link java.io.File} object.
     */
    public Path templateRoot() {
        return templateRoot;
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
     * 打印 coreConfig 的信息
     */
    public void printInfo() {
        Ansi.ansi()
                .green("Y 服务器 IP 地址                       \t -->\t " + Arrays.stream(ignore(() -> getLocalIPAddress(c -> c instanceof Inet4Address), new InetAddress[]{})).map(InetAddress::getHostAddress).collect(Collectors.joining(", ", "[", "]"))).ln()
                .green("Y 端口号                               \t -->\t " + port).ln()
                .green("Y 允许的请求源                         \t -->\t " + allowedOrigin).ln()
                .green("Y 模板根目录                           \t -->\t " + templateRoot.toString()).ln()
                .green("Y 是否开启 https                       \t -->\t " + (httpsEnabled ? "是" : "否")).ln()
                .green("Y 证书路径                            \t -->\t " + (sslPath != null ? sslPath.toString() : "")).ln()
                .green("Y 证书密码                            \t -->\t *****").ln()
                .green("Y 数据源 URL                           \t -->\t " + dataSourceUrl).ln()
                .green("Y 数据源 用户名                        \t -->\t " + dataSourceUsername).ln()
                .green("Y 数据源 连接密码                      \t -->\t *****").ln()
                .green("Y 数据源 连接参数                      \t -->\t " + Arrays.toString(dataSourceParameters)).println();
    }

}
