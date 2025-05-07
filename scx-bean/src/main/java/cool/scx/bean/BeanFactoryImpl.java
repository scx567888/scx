package cool.scx.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class BeanFactoryImpl implements BeanFactory {

    private final Map<String, BeanContext> beanContextMap = new ConcurrentHashMap<>();
    private final List<BeanDependencyResolver> beanDependencyResolvers = new ArrayList<>();

    @Override
    public Object getBean(String name) {
        var beanContext = getBeanContext(name);
        return beanContext.getBean(this);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        var beanContext = getBeanContext(requiredType);
        return (T) beanContext.getBean(this);
    }

    @Override
    public void registerBean(String name, Object instance) {
        registerBeanContext(name, new BeanContextImpl(new InstanceBeanCreator(instance), true));
    }

    @Override
    public void registerBeanClass(String name, Class<?> beanClass) {
        // todo 这里是否单例 需要 通过注解判断 暂时全部采用 单例
        registerBeanContext(name, new BeanContextImpl(new AnnotationConfigBeanCreator(beanClass), true));
    }

    @Override
    public void registerBeanContext(String name, BeanContext beanContext) {
        beanContextMap.put(name, beanContext);
    }

    @Override
    public void addBeanDependencyResolver(BeanDependencyResolver beanDependencyResolver) {
        this.beanDependencyResolvers.add(beanDependencyResolver);
    }

    @Override
    public List<BeanDependencyResolver> beanDependencyResolvers() {
        return beanDependencyResolvers;
    }

    @Override
    public void initializeBeans() {
        for (var entry : beanContextMap.values()) {
            entry.getBean(this);
        }
    }

    @Override
    public String[] getBeanNames() {
        return beanContextMap.keySet().toArray(String[]::new);
    }

    private BeanContext getBeanContext(String name) {
        var beanContext = beanContextMap.get(name);
        if (beanContext == null) {
            throw new IllegalArgumentException("未找到任何符合名称的 bean !!! name = " + name);
        }
        return beanContext;
    }

    private BeanContext getBeanContext(Class<?> requiredType) {
        var beanContexts = beanContextMap.values();
        var list = new ArrayList<BeanContext>();
        for (var beanContext : beanContexts) {
            if (requiredType.isAssignableFrom(beanContext.beanClass())) {
                list.add(beanContext);
            }
        }
        var size = list.size();
        if (size == 0) {
            throw new IllegalArgumentException("未找到任何符合类型的 bean !!! class = " + requiredType.getName());
        }
        if (size > 1) {
            throw new IllegalArgumentException("找到多个符合类型的 bean !!! class = " + requiredType.getName() + " 已找到 = " + list.stream().map(c -> c.beanClass().getName()).toList());
        }
        return list.get(0);
    }

}
