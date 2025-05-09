package cool.scx.bean.provider.x;


import cool.scx.bean.exception.BeanCreationException;

import java.util.ArrayList;
import java.util.List;

public class CircularDependencyChecker {

    /// 保存依赖链路
    private static final ThreadLocal<List<DependencyContext>> CURRENTLY_CREATING = ThreadLocal.withInitial(ArrayList::new);

    public static void startDependencyCheck(DependencyContext dependentContext) throws BeanCreationException {
        // 获取当前的依赖链
        var creatingList = CURRENTLY_CREATING.get();

        // 检查依赖冲突
        var b = checkContains(creatingList, dependentContext);
        if (b) {
            // 如果当前构建的类的类型在依赖链中，说明发生了循环依赖
            var message = "xxx";
            throw new BeanCreationException("在创建类 " + dependentContext.beanClass() + "时, 检测到构造函数参数循环依赖，依赖链 = [" + message + "]");
        }

        // 4. 将当前参数添加到依赖链中
        creatingList.add(dependentContext);

    }

    public static void endDependencyCheck() {
        var creatingList = CURRENTLY_CREATING.get();
        creatingList.removeLast();
    }

    public static boolean checkContains(List<DependencyContext> creatingList, DependencyContext context) {
        for (var beanProvider : creatingList) {
            if (beanProvider.beanClass() == context.beanClass()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkAllArePrototype(List<DependencyContext> creatingList) {
        for (var beanClass : creatingList) {
            if (beanClass.singleton()) {
                return false;
            }
        }
        return true;
    }
    
}
