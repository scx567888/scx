package cool.scx.bean.dependency;


import cool.scx.bean.exception.BeanCreationException;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.bean.dependency.DependencyContext.Type.CONSTRUCTOR;

/// 循环依赖检测器, 负责检测并处理循环依赖链条。
///
/// 为了确保循环依赖的检测正确性，设计上假设：
///
/// 【DependencyContext 幂等性假设】
/// - 在同一条依赖链（CURRENT_DEPENDENCY_CHAIN）内，
/// 如果出现相同的 beanClass（即类对象）多次，
/// 则这些 DependencyContext 实例的关键属性（singleton、type 等）
/// 必然一致。
///
/// 该假设基于以下设计保证：
/// - 依赖链是线性推进的，每次依赖的创建都是按照顺序发生的，不会跳跃或分支。
/// - 每个 DependencyContext 实例在第一次创建时即被固定下来，并且不会发生变化。
/// - 使用 ThreadLocal 确保每个线程的依赖链独立，避免线程间相互干扰。
///
/// 在此假设下，我们可以简单地从创建链中提取循环依赖的子链，而无需手动添加当前依赖的 context。
///
/// ⚠️ 请注意，未来引入更复杂的功能（如动态 Scope、并发构建等）时, 需要重新评估此假设是否仍然有效。
///
/// @author scx567888
/// @version 0.0.1
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
            var unsolvableCycleType = isUnsolvableCycle(circularDependencyChain);
            if (unsolvableCycleType != null) {
                //3, 创建友好的错误提示
                var message = buildCycleMessage(dependencyChain, dependentContext);
                var why = switch (unsolvableCycleType) {
                    case CONSTRUCTOR -> "构造函数循环依赖";
                    case ALL_PROTOTYPE -> "多例循环依赖";
                };
                throw new BeanCreationException("在创建类 " + dependentContext.beanClass() + "时, 检测到无法解决的" + why + ": \n\n" + message);
            }
        }

        // 4. 将当前参数添加到依赖链中
        dependencyChain.add(dependentContext);

    }

    public static void endDependencyCheck() {
        var dependencyChain = CURRENT_DEPENDENCY_CHAIN.get();
        dependencyChain.removeLast();
    }

    /// 获取依赖链条
    public static List<DependencyContext> getCurrentDependencyChain() {
        return CURRENT_DEPENDENCY_CHAIN.get();
    }

    /// 提取循环依赖链条。
    /// 根据当前依赖链（creatingList），从链条中提取出一个循环依赖的子链。
    /// 该方法假设，当前依赖上下文（context）与循环链条中第一次出现的相同 beanClass 的
    /// DependencyContext 实例具有相同的关键属性（singleton、type 等），因此我们只需要
    /// 从创建链中提取相应的子链，而不需要将当前 context 额外加入。
    /// 这一设计保证是建立在以下前提之上的：
    ///  - 在依赖链中，同一类的多个 DependencyContext 实例的属性（如类型、作用域等）是一致的。
    ///  - 因为创建是线性的，每次依赖的实例都是由上下文顺序逐步推进的，没有突变。
    /// 这种方式有助于减少冗余和避免不必要的计算，同时保持循环依赖的准确检测。
    private static List<DependencyContext> extractCircularDependencyChain(List<DependencyContext> creatingList, DependencyContext context) {
        var cycleStartIndex = findCycleStartIndex(creatingList, context);
        if (cycleStartIndex == -1) {
            return null;
        } else {
            // 此处无需拼接 context
            return creatingList.subList(cycleStartIndex, creatingList.size());
        }
    }

    public static List<DependencyContext> extractCircularDependencyChain(List<DependencyContext> creatingList, Class<?> beanClass) {
        var cycleStartIndex = findCycleStartIndex(creatingList, beanClass);
        if (cycleStartIndex == -1) {
            return null;
        } else {
            // 此处无需拼接 context
            return creatingList.subList(cycleStartIndex, creatingList.size());
        }
    }

    private static int findCycleStartIndex(List<DependencyContext> creatingList, DependencyContext context) {
        for (int i = 0; i < creatingList.size(); i = i + 1) {
            if (creatingList.get(i).beanClass() == context.beanClass()) {
                return i;
            }
        }
        return -1;
    }

    public static int findCycleStartIndex(List<DependencyContext> creatingList, Class<?> beanClass) {
        for (int i = 0; i < creatingList.size(); i = i + 1) {
            if (creatingList.get(i).beanClass() == beanClass) {
                return i;
            }
        }
        return -1;
    }

    /// 是否是无法解决的循环
    public static UnsolvableCycleType isUnsolvableCycle(List<DependencyContext> circularDependencyChain) {
        // 1, 检查链路中是否有构造器注入类型的依赖, 构造器注入 => 无法解决
        // 确实在某些情况下 如: A 类 构造器注入 b, B 类 字段注入 a, 
        // 我们可以通过先创建 半成品 b, 再创建 a, 然后再 b.a = a 来完成创建
        // 但这违反了一个规则 及 构造函数中拿到的永远应该是一个 完整对象 而不是半成品 因为用户有可能在 A 的构造函数中, 调用 b.a 
        // 此时因为 b 是一个半成品对象, 便会引发空指针, 所以我们从根源上禁止 任何链路上存在 构造器循环依赖

        for (var c : circularDependencyChain) {
            if (c.type() == CONSTRUCTOR) {
                return UnsolvableCycleType.CONSTRUCTOR;// 无法解决
            }
        }

        // 2, 此时严格来说整个循环链条中 全部都是 字段注入
        // 关于 字段注入 严格来说 只要整个链条中存在任意一个单例对象 便可以打破无限循环 
        // 所以我们在此处进行 检测 整个链路是否存在任意一个单例

        for (var c : circularDependencyChain) {
            if (c.singleton()) {
                return null; // 只要存在单例 就表示能够解决
            }
        }

        // 3, 如果链路中没有单例（只有多例），无法解决循环依赖
        return UnsolvableCycleType.ALL_PROTOTYPE;
    }

    /// 构建循环链的错误信息
    private static String buildCycleMessage(List<DependencyContext> circularChain, DependencyContext dependentContext) {
        // 1. 找到循环起始点
        var cycleStartIndex = findCycleStartIndex(circularChain, dependentContext);
        // 2. 构建可视化链条
        var sb = new StringBuilder();

        for (int i = 0; i < circularChain.size(); i = i + 1) {
            var ctx = circularChain.get(i);
            var baseInfo = ctx.beanClass().getName() + " " + getDependencyDescription(ctx) + "\n";

            if (i < cycleStartIndex) { // 不处于循环中
                sb.append("    ").append(baseInfo);
                sb.append("              🡻\n");
            } else if (i == cycleStartIndex) {// 循环开始
                sb.append("╭─➤ ").append(baseInfo);
                sb.append("|             🡻\n");
                // 循环结束 换句话说 起始等于结束 所以是自我引用
                if (i == circularChain.size() - 1) {
                    sb.append("╰───────── (自我引用) \n");
                }
            } else if (i < circularChain.size() - 1) {// 循环节点
                sb.append("|   ").append(baseInfo);
                sb.append("|             🡻\n");
            } else { // 闭环
                sb.append("╰── ").append(baseInfo);
            }
        }

        return sb.toString();
    }

    private static String getDependencyDescription(DependencyContext dependentContext) {
        return switch (dependentContext.type()) {
            case CONSTRUCTOR -> "(构造参数: " + dependentContext.parameter().name() + ")";
            case FIELD -> "[字段: " + dependentContext.fieldInfo().name() + "]";
        };
    }

    public enum UnsolvableCycleType {

        CONSTRUCTOR,

        ALL_PROTOTYPE

    }

}
