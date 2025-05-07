package cool.scx.bean.x;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactoryImpl implements BeanFactory {

    private final Map<String, BeanCreator> beanCreatorMap = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String name) {
        var bean = beanCreatorMap.get(name);
        if (bean == null) {
            throw new IllegalArgumentException("未找到任何符合名称的 bean !!! name = " + name);
        }
        return bean.create(this);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        var beanCreators = beanCreatorMap.values();
        var list = new ArrayList<BeanCreator>();
        for (var beanCreator : beanCreators) {
            if (requiredType.isAssignableFrom(beanCreator.beanClass())) {
                list.add(beanCreator);
            }
        }
        var size = list.size();
        if (size == 0) {
            throw new IllegalArgumentException("未找到任何符合类型的 bean !!! class = " + requiredType.getName());
        }
        if (size > 1) {
            throw new IllegalArgumentException("找到多个符合类型的 bean !!! class = " + requiredType.getName() + " 已找到 = " + list.stream().map(c -> c.beanClass().getName()).toList());
        }
        return (T) list.get(0).create(this);
    }

    @Override
    public void registerBeanCreator(String name, BeanCreator beanCreator) {
        beanCreatorMap.put(name, beanCreator);
    }

    @Override
    public void initializeBeans() {
        for (var entry : beanCreatorMap.values()) {
            entry.create(this);
        }
    }

    @Override
    public String[] getBeanNames() {
        return beanCreatorMap.keySet().toArray(String[]::new);
    }

}
