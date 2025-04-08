package cool.scx.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ResolvableType;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor springAutowiredAnnotationBeanPostProcessor;

    public AutowiredAnnotationBeanPostProcessor() {
        this.springAutowiredAnnotationBeanPostProcessor = new org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return springAutowiredAnnotationBeanPostProcessor.postProcessAfterInitialization(bean, beanName);
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        springAutowiredAnnotationBeanPostProcessor.setBeanFactory(new org.springframework.beans.factory.BeanFactory() {
            
            @Override
            public Object getBean(String name) throws BeansException {
                return beanFactory.getBean(name);
            }

            @Override
            public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
                return beanFactory.getBean(name, requiredType);
            }

            @Override
            public Object getBean(String name, Object... args) throws BeansException {
                return beanFactory.getBean(name, args);
            }

            @Override
            public <T> T getBean(Class<T> requiredType) throws BeansException {
                return beanFactory.getBean(requiredType);
            }

            @Override
            public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
                return beanFactory.getBean(requiredType, args);
            }

            @Override
            public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
                return beanFactory.getBeanProvider(requiredType);
            }

            @Override
            public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
                return beanFactory.getBeanProvider(requiredType);
            }

            @Override
            public boolean containsBean(String name) {
                return beanFactory.containsBean(name);
            }

            @Override
            public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
                return beanFactory.isSingleton(name);
            }

            @Override
            public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
                return beanFactory.isPrototype(name);
            }

            @Override
            public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
                return beanFactory.isTypeMatch(name, typeToMatch);
            }

            @Override
            public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
                return beanFactory.isTypeMatch(name, typeToMatch);
            }

            @Override
            public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
                return beanFactory.getType(name);
            }

            @Override
            public Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
                return beanFactory.getType(name, allowFactoryBeanInit);
            }

            @Override
            public String[] getAliases(String name) {
                return beanFactory.getAliases(name);
            }
        });
    }

}
