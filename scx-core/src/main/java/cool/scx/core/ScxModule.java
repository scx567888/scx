package cool.scx.core;

import java.nio.file.Path;
import java.util.List;

/**
 * Scx 模块接口 , 自定义模块必须实现此接口
 * <p>
 * 生命周期请参阅方法说明
 *
 * @author scx567888
 * @version 1.1.2
 */
public interface ScxModule {

    /**
     * 核心模块初始化完成调用
     * 注意请不要阻塞此方法
     */
    default void start(Scx scx) {

    }

    /**
     * 项目停止或结束时调用
     * 注意请不要阻塞此方法
     */
    default void stop(Scx scx) {

    }

    /**
     * 模块名称
     *
     * @return name
     */
    default String name() {
        return this.getClass().getSimpleName();
    }

    /**
     * 包含的 class 列表
     *
     * @return classList
     */
    List<Class<?>> classList();

    /**
     * 所在路径
     *
     * @return rootPath
     */
    Path rootPath();

}
