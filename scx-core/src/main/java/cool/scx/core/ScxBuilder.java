package cool.scx.core;

import cool.scx.common.util.StringUtils;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxConfigSource;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.config.source.ArgsConfigSource;
import cool.scx.config.source.JsonFileConfigSource;
import cool.scx.config.source.MapConfigSource;
import cool.scx.core.enumeration.ScxCoreFeature;
import cool.scx.http.helidon.HelidonHttpServerOptions;

import java.util.*;

/**
 * Scx 构建器
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxBuilder {

    /**
     * 默认配置键值对, 以便在没有配置文件的时候可以使项目正确启动
     */
    private static final Map<String, Object> DEFAULT_CONFIG_MAP = initDefaultConfigMap();

    /**
     * 默认的核心包 APP KEY (密码) , 注意请不要在您自己的模块中使用此常量 , 非常不安全
     */
    private static final String DEFAULT_APP_KEY = "SCX-123456";

    /**
     * 默认配置文件 路径
     */
    private static final String DEFAULT_SCX_CONFIG_PATH = "AppRoot:scx-config.json";

    /**
     * 用来存储临时待添加的 scxModules
     */
    private final List<ScxModule> scxModules = new ArrayList<>();

    /**
     * 用来存储临时待添加的 scxFeatureConfig
     */
    private final ScxFeatureConfig scxFeatureConfig = new ScxFeatureConfig();

    /**
     * 配置源
     */
    private final List<ScxConfigSource> scxConfigSources = new ArrayList<>();

    /**
     * 用来存储临时待添加的 外部参数
     */
    private String[] args = new String[]{};

    /**
     * 用来存储临时待添加的 mainClass
     */
    private Class<?> mainClass = null;

    /**
     * 用来存储临时待添加的 appKey
     */
    private String appKey;
    private HelidonHttpServerOptions defaultHttpServerOptions;

    /**
     * 构造函数
     */
    public ScxBuilder() {
        appKey = DEFAULT_APP_KEY;
        defaultHttpServerOptions = new HelidonHttpServerOptions();
    }

    /**
     * a
     *
     * @return a
     */
    private static Map<String, Object> initDefaultConfigMap() {
        var tempMap = new LinkedHashMap<String, Object>();
        tempMap.put("scx.port", 8080);
        tempMap.put("scx.tombstone", false);
        tempMap.put("scx.allowed-origin", "*");
        tempMap.put("scx.template.root", "AppRoot:/c/");
        tempMap.put("scx.static-servers", new Object[0]);
        tempMap.put("scx.https.enabled", false);
        tempMap.put("scx.https.ssl-path", "");
        tempMap.put("scx.https.ssl-password", "");
        tempMap.put("scx.data-source.host", "127.0.0.1");
        tempMap.put("scx.data-source.port", 3306);
        tempMap.put("scx.data-source.database", "");
        tempMap.put("scx.data-source.username", "");
        tempMap.put("scx.data-source.password", "");
        tempMap.put("scx.data-source.parameters", new HashSet<>());
        tempMap.put("scx.logging.default.level", "ERROR");
        tempMap.put("scx.logging.default.type", "CONSOLE");
        tempMap.put("scx.logging.default.stored-directory", "AppRoot:logs");
        tempMap.put("scx.logging.default.stack-trace", false);
        return tempMap;
    }

    /**
     * 初始化 AppKey
     *
     * @param appKey a
     * @return a
     */
    private static String checkAppKey(String appKey) {
        if (StringUtils.isBlank(appKey)) {
            throw new IllegalArgumentException("AppKey cannot be set empty");
        } else if (DEFAULT_APP_KEY.equals(appKey)) {
            System.err.println("注意!!! 未设置 APP_KEY ,已采用 DEFAULT_APP_KEY , 这是非常不安全的 , 建议设置自定义的 APP_KEY !!!");
        }
        return appKey;
    }

    /**
     * <p>initMainClass.</p>
     *
     * @param mainClass a {@link java.lang.Class} object
     * @return a {@link java.lang.Class} object
     */
    private static Class<?> checkMainClass(Class<?> mainClass) {
        //1,检测 mainClass 是否正确
        if (mainClass == null) {
            throw new IllegalArgumentException("MainClass must not be empty !!! ");
        }
        return mainClass;
    }

    private static String getScxConfigPath(String[] args) {
        var scxConfig = new ScxConfig(ArgsConfigSource.of(args));
        var scxConfigPath = scxConfig.get("scx.config.path", String.class);
        return scxConfigPath != null ? scxConfigPath : DEFAULT_SCX_CONFIG_PATH;
    }

    /**
     * a
     */
    public Scx run() {
        return this.build().run();
    }

    /**
     * 构建
     *
     * @return a
     */
    public Scx build() {
        //检查 appKey
        checkAppKey(appKey);
        //检查 mainClass
        checkMainClass(mainClass);
        //处理数据源
        var scxEnvironment = new ScxEnvironment(mainClass);
        //配置源 注意顺序 以保证可以逐个覆盖
        var defaultMapConfigSource = MapConfigSource.of(DEFAULT_CONFIG_MAP);
        var defaultJsonFileConfigSource = JsonFileConfigSource.of(scxEnvironment.getPathByAppRoot(getScxConfigPath(args)));
        var defaultArgsConfigSource = ArgsConfigSource.of(args);
        scxConfigSources.add(defaultMapConfigSource);
        scxConfigSources.add(defaultJsonFileConfigSource);
        scxConfigSources.add(defaultArgsConfigSource);
        //创建 scx 实例
        var scxConfig = new ScxConfig(scxConfigSources.toArray(ScxConfigSource[]::new));
        return new Scx(scxEnvironment, appKey, scxFeatureConfig, scxConfig, scxModules.toArray(ScxModule[]::new), defaultHttpServerOptions);
    }

    /**
     * 添加多个模块
     *
     * @param modules a
     * @return a
     */
    public ScxBuilder addModule(ScxModule... modules) {
        this.scxModules.addAll(Arrays.asList(modules));
        return this;
    }

    /**
     * 添加多个模块
     *
     * @param mainClass a
     * @return a
     */
    public ScxBuilder setMainClass(Class<?> mainClass) {
        this.mainClass = mainClass;
        return this;
    }

    /**
     * 添加多个模块
     *
     * @param appKey a
     * @return a
     */
    public ScxBuilder setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    /**
     * 添加 外部参数
     *
     * @param args a
     * @return a
     */
    public ScxBuilder setArgs(String... args) {
        this.args = args;
        return this;
    }

    /**
     * 设置配置内容
     *
     * @param scxFeature s
     * @param state      s
     * @return a
     */
    public ScxBuilder configure(ScxCoreFeature scxFeature, boolean state) {
        scxFeatureConfig.set(scxFeature, state);
        return this;
    }

    public ScxBuilder setDefaultHttpServerOptions(HelidonHttpServerOptions options) {
        this.defaultHttpServerOptions = options;
        return this;
    }

}
