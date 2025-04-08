package cool.scx.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.ResolvableType;

import java.util.function.Function;

/// 目前内部使用 spring 做实现
public class DefaultListableBeanFactory implements BeanFactory {

    private final org.springframework.beans.factory.support.DefaultListableBeanFactory springDefaultListableBeanFactory;

    public DefaultListableBeanFactory() {
        this.springDefaultListableBeanFactory = new org.springframework.beans.factory.support.DefaultListableBeanFactory();
    }

    @Override
    public Object getBean(String name) {
        return springDefaultListableBeanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return springDefaultListableBeanFactory.getBean(name, requiredType);
    }

    @Override
    public Object getBean(String name, Object... args) {
        return springDefaultListableBeanFactory.getBean(name, args);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return springDefaultListableBeanFactory.getBean(requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) {
        return springDefaultListableBeanFactory.getBean(requiredType, args);
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
        return springDefaultListableBeanFactory.getBeanProvider(requiredType);
    }

    @Override
    public <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType) {
        return springDefaultListableBeanFactory.getBeanProvider(requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return springDefaultListableBeanFactory.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return springDefaultListableBeanFactory.isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) {
        return springDefaultListableBeanFactory.isPrototype(name);
    }

    @Override
    public boolean isTypeMatch(String name, ResolvableType typeToMatch) {
        return springDefaultListableBeanFactory.isTypeMatch(name, typeToMatch);
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) {
        return springDefaultListableBeanFactory.isTypeMatch(name, typeToMatch);
    }

    @Override
    public Class<?> getType(String name) {
        return springDefaultListableBeanFactory.getType(name);
    }

    @Override
    public Class<?> getType(String name, boolean allowFactoryBeanInit) {
        return springDefaultListableBeanFactory.getType(name, allowFactoryBeanInit);
    }

    @Override
    public String[] getAliases(String name) {
        return springDefaultListableBeanFactory.getAliases(name);
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        springDefaultListableBeanFactory.registerBeanDefinition(beanName, new org.springframework.beans.factory.config.BeanDefinition() {
            
            @Override
            public void setAttribute(String name, Object value) {
                beanDefinition.setAttribute(name,value);
            }

            @Override
            public Object getAttribute(String name) {
                return beanDefinition.getAttribute(name);
            }

            @Override
            public Object removeAttribute(String name) {
                return beanDefinition.removeAttribute(name);
            }

            @Override
            public boolean hasAttribute(String name) {
                return beanDefinition.hasAttribute(name);
            }

            @Override
            public String[] attributeNames() {
                return beanDefinition.attributeNames();
            }

            @Override
            public void setParentName(String parentName) {
                beanDefinition.setParentName(parentName);
            }

            @Override
            public String getParentName() {
                return beanDefinition.getParentName();
            }

            @Override
            public void setBeanClassName(String beanClassName) {
                beanDefinition.setBeanClassName(beanClassName);
            }

            @Override
            public String getBeanClassName() {
                return beanDefinition.getBeanClassName();
            }

            @Override
            public void setScope(String scope) {
                beanDefinition.setScope(scope);
            }

            @Override
            public String getScope() {
                return beanDefinition.getScope();
            }

            @Override
            public void setLazyInit(boolean lazyInit) {
                beanDefinition.setLazyInit(lazyInit);
            }

            @Override
            public boolean isLazyInit() {
                return beanDefinition.isLazyInit();
            }

            @Override
            public void setDependsOn(String... dependsOn) {
                beanDefinition.setDependsOn(dependsOn);
            }

            @Override
            public String[] getDependsOn() {
                return beanDefinition.getDependsOn();
            }

            @Override
            public void setAutowireCandidate(boolean autowireCandidate) {
                beanDefinition.setAutowireCandidate(autowireCandidate);
            }

            @Override
            public boolean isAutowireCandidate() {
                return beanDefinition.isAutowireCandidate();
            }

            @Override
            public void setPrimary(boolean primary) {
                beanDefinition.setPrimary(primary);
            }

            @Override
            public boolean isPrimary() {
                return beanDefinition.isPrimary();
            }

            @Override
            public void setFallback(boolean fallback) {
                beanDefinition.setFallback(fallback);
            }

            @Override
            public boolean isFallback() {
                return beanDefinition.isFallback();
            }

            @Override
            public void setFactoryBeanName(String factoryBeanName) {
                beanDefinition.setFactoryBeanName(factoryBeanName);
            }

            @Override
            public String getFactoryBeanName() {
                return beanDefinition.getFactoryBeanName();
            }

            @Override
            public void setFactoryMethodName(String factoryMethodName) {
                beanDefinition.setFactoryMethodName(factoryMethodName);
            }

            @Override
            public String getFactoryMethodName() {
                return beanDefinition.getFactoryMethodName();
            }

            @Override
            public ConstructorArgumentValues getConstructorArgumentValues() {
                return beanDefinition.getConstructorArgumentValues();
            }

            @Override
            public MutablePropertyValues getPropertyValues() {
                return beanDefinition.getPropertyValues();
            }

            @Override
            public void setInitMethodName(String initMethodName) {
                beanDefinition.setInitMethodName(initMethodName);
            }

            @Override
            public String getInitMethodName() {
                return beanDefinition.getInitMethodName();
            }

            @Override
            public void setDestroyMethodName(String destroyMethodName) {
                beanDefinition.setDestroyMethodName(destroyMethodName);
            }

            @Override
            public String getDestroyMethodName() {
                return beanDefinition.getDestroyMethodName();
            }

            @Override
            public void setRole(int role) {
                beanDefinition.setRole(role);
            }

            @Override
            public int getRole() {
                return beanDefinition.getRole();
            }

            @Override
            public void setDescription(String description) {
                beanDefinition.setDescription(description);
            }

            @Override
            public String getDescription() {
                return beanDefinition.getDescription();
            }

            @Override
            public ResolvableType getResolvableType() {
                return beanDefinition.getResolvableType();
            }

            @Override
            public boolean isSingleton() {
                return beanDefinition.isSingleton();
            }

            @Override
            public boolean isPrototype() {
                return beanDefinition.isPrototype();
            }

            @Override
            public boolean isAbstract() {
                return beanDefinition.isAbstract();
            }

            @Override
            public String getResourceDescription() {
                return beanDefinition.getResourceDescription();
            }

            @Override
            public org.springframework.beans.factory.config.BeanDefinition getOriginatingBeanDefinition() {
                return beanDefinition.getOriginatingBeanDefinition();
            }

            @Override
            public boolean hasConstructorArgumentValues() {
                return beanDefinition.hasConstructorArgumentValues();
            }

            @Override
            public boolean hasPropertyValues() {
                return beanDefinition.hasPropertyValues();
            }

            @Override
            public Object getSource() {
                return beanDefinition.getSource();
            }

            @Override
            public <T> T computeAttribute(String name, Function<String, T> computeFunction) {
                return beanDefinition.computeAttribute(name, computeFunction);
            }
        });
    }

    public String[] getBeanDefinitionNames() {
        return springDefaultListableBeanFactory.getBeanDefinitionNames();
    }
    
    public void preInstantiateSingletons() {
        springDefaultListableBeanFactory.preInstantiateSingletons();
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        springDefaultListableBeanFactory.addBeanPostProcessor(new org.springframework.beans.factory.config.BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            }

        });
    }
    
    public void setAllowCircularReferences(boolean aBoolean) {
        springDefaultListableBeanFactory.setAllowCircularReferences(aBoolean);
    }
    
}
