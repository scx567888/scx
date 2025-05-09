package cool.scx.bean.dependency;


import cool.scx.bean.exception.BeanCreationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static cool.scx.bean.dependency.DependencyContext.Type.CONSTRUCTOR;

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
    /// todo 这个方法有待确认是否正确
    public static boolean isUnsolvableCycle(List<DependencyContext> creatingList, DependencyContext context) {
        // 如果当前上下文是构造器注入类型，直接返回无法解决的循环依赖
        if (context.type() == CONSTRUCTOR) {
            return true;
        }

        // 检查链路中是否有构造器注入类型的依赖
        for (DependencyContext dependencyContext : creatingList) {
            if (dependencyContext.type() == DependencyContext.Type.CONSTRUCTOR) {
                return true; // 构造器注入 => 无法解决
            }
        }

        // 检查链路中是否有单例
        boolean hasSingleInstance = false;
        for (DependencyContext dependencyContext : creatingList) {
            if (dependencyContext.singleton()) {
                hasSingleInstance = true;
                break;
            }
        }
        
        // 如果链路中没有单例（只有多例），无法解决循环依赖
        if (!hasSingleInstance && !context.singleton()) {
            return true;
        }
        
        // 其余情况都能解决
        return false;
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
