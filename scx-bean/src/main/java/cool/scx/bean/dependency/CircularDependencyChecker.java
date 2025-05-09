package cool.scx.bean.dependency;


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
        // 1, 初次检查, 如果当前构建的类的类型在依赖链中，说明发生了循环依赖
        var containsCycle = containsCycle(creatingList, dependentContext);
        if (containsCycle) {
            //2, 检查是否是不可解决的循环依赖
            var isUnsolvableCycle = isUnsolvableCycle(creatingList, dependentContext);
            if (isUnsolvableCycle) {
                var message = buildCycleMessage(creatingList, dependentContext);
                throw new BeanCreationException("在创建类 " + dependentContext.beanClass() + "时, 检测到循环依赖，依赖链 = [" + message + "]");
            }
        }

        // 4. 将当前参数添加到依赖链中
        creatingList.add(dependentContext);

    }

    public static void endDependencyCheck() {
        var creatingList = CURRENTLY_CREATING.get();
        creatingList.removeLast();
    }

    /// 判断当前依赖链是否形成循环
    public static boolean containsCycle(List<DependencyContext> creatingList, DependencyContext context) {
        for (var beanProvider : creatingList) {
            if (beanProvider.beanClass() == context.beanClass()) {
                return true;
            }
        }
        return false;
    }

    /// 是否是无法解决的循环
    public static boolean isUnsolvableCycle(List<DependencyContext> creatingList, DependencyContext context) {
        // todo 如何判断无法解决
    }

    /// 构建循环链的错误信息
    private static String buildCycleMessage(List<DependencyContext> stack, DependencyContext context) {
        var cycleNames = new ArrayList<String>();
        for (DependencyContext dependencyContext : stack) {
            var name = dependencyContext.beanClass().getName();
            cycleNames.add(name);
        }
        cycleNames.add(context.beanClass().getName());
        return String.join(" → ", cycleNames);
    }

}
