package cool.scx.bean.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class BeanFactoryImpl implements BeanFactory {

    private final Map<String, BeanContext> beanContextMap = new ConcurrentHashMap<>();
    private final List<BeanInjector> beanInjectors = new ArrayList<>();
    private final List<BeanProcessor> beanProcessors = new ArrayList<>();

    @Override
    public Object getBean(String name) {
        var beanContext = getBeanContext(name);
        return beanContext.createAndInject(this);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        var beanContext = getBeanContext(requiredType);
        return (T) beanContext.createAndInject(this);
    }

    @Override
    public void registerBeanContext(String name, BeanContext beanContext) {
        beanContextMap.put(name, beanContext);
    }

    @Override
    public BeanContext getBeanContext(String name) {
        var beanContext = beanContextMap.get(name);
        if (beanContext == null) {
            throw new IllegalArgumentException("未找到任何符合名称的 BeanContext !!! name = " + name);
        }
        return beanContext;
    }

    @Override
    public BeanContext getBeanContext(Class<?> requiredType) {
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

    @Override
    public void addBeanInjector(BeanInjector beanInjector) {
        this.beanInjectors.add(beanInjector);
    }

    @Override
    public List<BeanInjector> beanInjectors() {
        return beanInjectors;
    }

    @Override
    public void addBeanProcessor(BeanProcessor beanProcessor) {
        this.beanProcessors.add(beanProcessor);
    }

    @Override
    public List<BeanProcessor> beanProcessors() {
        return beanProcessors;
    }

    @Override
    public void initializeBeans() {
        for (var entry : beanContextMap.values()) {
            entry.createAndInject(this);
        }
    }

    @Override
    public String[] getBeanNames() {
        return beanContextMap.keySet().toArray(String[]::new);
    }

}
