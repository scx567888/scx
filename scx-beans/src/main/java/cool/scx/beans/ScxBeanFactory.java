package cool.scx.beans;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

/**
 * ScxBeanFactory 用于创建类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ScxBeanFactory {

    /**
     * Spring 的 BeanFactory
     */
    private final DefaultListableBeanFactory springBeanFactory;

    /**
     * <p>Constructor for ScxBeanFactory.</p>
     */
    public ScxBeanFactory() {
        this(new ScxBeanFactoryOptions());
    }

    /**
     * <p>Constructor for ScxBeanFactory.</p>
     *
     * @param options a {@link cool.scx.beans.ScxBeanFactoryOptions} object
     */
    public ScxBeanFactory(ScxBeanFactoryOptions options) {
        this.springBeanFactory = initBeanFactory(options);
    }

    /**
     * <p>initBeanFactory.</p>
     *
     * @param options a {@link cool.scx.beans.ScxBeanFactoryOptions} object
     * @return a {@link org.springframework.beans.factory.support.DefaultListableBeanFactory} object
     */
    private static DefaultListableBeanFactory initBeanFactory(ScxBeanFactoryOptions options) {
        var beanFactory = new DefaultListableBeanFactory();
        //这里添加一个 bean 的后置处理器以便可以使用 @Autowired 注解
        var beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        beanPostProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(beanPostProcessor);
        //只有 开启标识时才 启用定时任务 这里直接跳过 后置处理器
        if (options.enableSchedulingWithAnnotation()) {
            //这里在添加一个 bean 的后置处理器 以便使用 定时任务 注解
            var scheduledAnnotationBeanPostProcessor = new ScheduledAnnotationBeanPostProcessor();
            scheduledAnnotationBeanPostProcessor.setBeanFactory(beanFactory);
            scheduledAnnotationBeanPostProcessor.setScheduler(options.scheduler());
            scheduledAnnotationBeanPostProcessor.afterSingletonsInstantiated();
            beanFactory.addBeanPostProcessor(scheduledAnnotationBeanPostProcessor);
        }
        //设置是否允许循环依赖 (默认禁止循环依赖)
        beanFactory.setAllowCircularReferences(options.allowCircularReferences());
        return beanFactory;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxBeanFactory refresh() {
        this.springBeanFactory.preInstantiateSingletons();
        return this;
    }

    /**
     * <p>getBean.</p>
     *
     * @param requiredType a {@link java.lang.Class} object.
     * @param <T>          a T object.
     * @return a T object.
     */
    public <T> T getBean(Class<T> requiredType) {
        return springBeanFactory.getBean(requiredType);
    }

    /**
     * <p>registerBean.</p>
     *
     * @param classList c
     * @return a
     */
    public ScxBeanFactory register(Class<?>... classList) {
        for (var c : classList) {
            var beanDefinition = new AnnotatedGenericBeanDefinition(c);
            //这里是为了兼容 spring context 的部分注解
            AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDefinition);
            springBeanFactory.registerBeanDefinition(c.getName(), beanDefinition);
        }
        return this;
    }

    /**
     * 移除 bean 定义
     *
     * @param beanName bean 名称
     * @return a
     */
    public ScxBeanFactory removeBeanDefinition(String beanName) {
        springBeanFactory.removeBeanDefinition(beanName);
        return this;
    }

    /**
     * a
     *
     * @return a
     */
    public String[] getBeanDefinitionNames() {
        return springBeanFactory.getBeanDefinitionNames();
    }

}
