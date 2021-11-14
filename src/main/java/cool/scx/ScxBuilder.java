package cool.scx;

import cool.scx.config.ScxFeatureConfig;
import cool.scx.enumeration.ScxFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Scx 构建器
 */
public final class ScxBuilder {

    /**
     * 用来存储临时待添加的 scxModules
     */
    private final List<ScxModule> scxModules = new ArrayList<>();

    /**
     * 用来存储临时待添加的 scxFeatureConfig
     */
    private final ScxFeatureConfig scxFeatureConfig = new ScxFeatureConfig();

    /**
     * 用来存储临时待添加的 外部参数
     */
    private String[] args = null;

    /**
     * 用来存储临时待添加的 mainClass
     */
    private Class<?> mainClass = null;

    /**
     * 用来存储临时待添加的 appKey
     */
    private String appKey = Scx.DEFAULT_APP_KEY;

    /**
     * 构造函数
     */
    public ScxBuilder() {

    }

    /**
     * 构建
     *
     * @return a
     */
    public Scx build() {
        return new Scx(mainClass, scxModules.toArray(ScxModule[]::new), appKey, scxFeatureConfig, args);
    }

    /**
     * 添加单个模块
     *
     * @param module m
     * @return m
     */
    public ScxBuilder addModule(ScxModule module) {
        scxModules.add(module);
        return this;
    }

    /**
     * 添加多个模块
     *
     * @param modules a
     * @return a
     */
    public ScxBuilder addModules(ScxModule... modules) {
        for (var module : modules) {
            this.addModule(module);
        }
        return this;
    }

    /**
     * 添加多个模块
     *
     * @param modules a
     * @return a
     */
    public ScxBuilder addModules(Iterable<? extends ScxModule> modules) {
        for (var module : modules) {
            this.addModule(module);
        }
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
    public ScxBuilder configure(ScxFeature scxFeature, boolean state) {
        scxFeatureConfig.configure(scxFeature, state);
        return this;
    }

}
