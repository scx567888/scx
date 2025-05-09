package cool.scx.bean.dependency;


import cool.scx.bean.exception.BeanCreationException;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.bean.dependency.DependencyContext.Type.CONSTRUCTOR;

public class CircularDependencyChecker {

    /// 保存依赖链路
    private static final ThreadLocal<List<DependencyContext>> CURRENT_DEPENDENCY_CHAIN = ThreadLocal.withInitial(ArrayList::new);

    public static void startDependencyCheck(DependencyContext dependentContext) throws BeanCreationException {
        // 获取当前的依赖链
        var dependencyChain = CURRENT_DEPENDENCY_CHAIN.get();

        // 1, 提取循环依赖链条 若循环依赖链条为空 则表示没有循环依赖
        var circularDependencyChain = extractCircularDependencyChain(dependencyChain, dependentContext);
        if (circularDependencyChain != null) {
            //2, 检查是否是不可解决的循环依赖
            var isUnsolvableCycle = isUnsolvableCycle(circularDependencyChain);
            if (isUnsolvableCycle) {
                //3, 创建友好的错误提示
                var message = buildCycleMessage(dependencyChain, dependentContext);
                throw new BeanCreationException("在创建类 " + dependentContext.beanClass() + "时, 检测到循环依赖，依赖链 = [" + message + "]");
            }
        }

        // 4. 将当前参数添加到依赖链中
        dependencyChain.add(dependentContext);

    }

    public static void endDependencyCheck() {
        var dependencyChain = CURRENT_DEPENDENCY_CHAIN.get();
        dependencyChain.removeLast();
    }

    /// 查找循环链条
    public static List<DependencyContext> extractCircularDependencyChain(List<DependencyContext> creatingList, DependencyContext context) {
        var cycleStartIndex = -1;
        for (int i = 0; i < creatingList.size(); i = i + 1) {
            if (creatingList.get(i).beanClass() == context.beanClass()) {
                cycleStartIndex = i;
                break;
            }
        }
        if (cycleStartIndex == -1) {
            return null;
        } else {
            return creatingList.subList(cycleStartIndex, creatingList.size());
        }
    }

    /// 是否是无法解决的循环
    public static boolean isUnsolvableCycle(List<DependencyContext> circularDependencyChain) {
        // 1, 检查链路中是否有构造器注入类型的依赖, 构造器注入 => 无法解决
        // 确实在某些情况下 如: A 类 构造器注入 b, B 类 字段注入 a, 
        // 我们可以通过先创建 半成品 b, 再创建 a, 然后再 b.a = a 来完成创建
        // 但这违反了一个规则 及 构造函数中拿到的永远应该是一个 完整对象 而不是半成品 因为用户有可能在 A 的构造函数中, 调用 b.a 
        // 此时因为 b 是一个半成品对象, 便会引发空指针, 所以我们从根源上禁止 任何链路上存在 构造器循环依赖

        for (var c : circularDependencyChain) {
            if (c.type() == CONSTRUCTOR) {
                return true;// 无法解决
            }
        }

        // 2, 此时严格来说整个循环链条中 全部都是 字段注入
        // 关于 字段注入 严格来说 只要整个链条中存在任意一个单例对象 便可以打破无限循环 
        // 所以我们在此处进行 检测 整个链路是否存在任意一个单例

        for (var c : circularDependencyChain) {
            if (c.singleton()) {
                return false; // 只要存在单例 就表示能够解决
            }
        }

        // 3, 如果链路中没有单例（只有多例），无法解决循环依赖
        return true;
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
