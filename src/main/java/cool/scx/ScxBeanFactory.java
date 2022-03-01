package cool.scx;

import cool.scx.config.ScxFeatureConfig;
import cool.scx.enumeration.ScxFeature;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;

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
    private final DefaultListableBeanFactory springBeanFactory = new DefaultListableBeanFactory();

    /**
     * a
     *
     * @param scheduledExecutorService a
     * @param scxFeatureConfig         a
     */
    public ScxBeanFactory(ScheduledExecutorService scheduledExecutorService, ScxFeatureConfig scxFeatureConfig) {
        //这里添加一个 bean 的后置处理器以便可以使用 @Autowired 注解
        var beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        beanPostProcessor.setBeanFactory(this.springBeanFactory);
        this.springBeanFactory.addBeanPostProcessor(beanPostProcessor);
        //只有 开启标识时才 启用定时任务 这里直接跳过 后置处理器
        if (scxFeatureConfig.getFeatureState(ScxFeature.ENABLE_SCHEDULING_WITH_ANNOTATION)) {
            //这里在添加一个 bean 的后置处理器 以便使用 定时任务 注解
            var scheduledAnnotationBeanPostProcessor = new ScheduledAnnotationBeanPostProcessor();
            scheduledAnnotationBeanPostProcessor.setBeanFactory(this.springBeanFactory);
            scheduledAnnotationBeanPostProcessor.setScheduler(scheduledExecutorService);
            scheduledAnnotationBeanPostProcessor.afterSingletonsInstantiated();
            this.springBeanFactory.addBeanPostProcessor(scheduledAnnotationBeanPostProcessor);
        }
    }

    /**
     * <p>registerBean.</p>
     *
     * @param classList c
     */
    public void registerBean(Collection<Class<?>> classList) {
        for (var c : classList) {
            springBeanFactory.registerBeanDefinition(c.getName(), BeanDefinitionBuilder.rootBeanDefinition(c).getBeanDefinition());
        }
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
     * 移除 bean 定义
     *
     * @param beanName bean 名称
     */
    public void removeBeanDefinition(String beanName) {
        springBeanFactory.removeBeanDefinition(beanName);
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
