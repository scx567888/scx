package cool.scx.bean.provider.injecting;

import cool.scx.bean.exception.BeanCreationException;

import java.util.ArrayList;
import java.util.List;

/// 循环依赖检查器 todo 循环依赖输出待美化
class CircularDependencyChecker {

    /// 保存依赖链路
    private static final ThreadLocal<List<DependentContext>> CURRENTLY_CREATING = ThreadLocal.withInitial(ArrayList::new);

    public static void startDependencyCheck(DependentContext context) throws BeanCreationException {
        var creatingList = CURRENTLY_CREATING.get();

        // 检测循环依赖
        if (checkContains(creatingList, context)) {
            boolean b = checkAllArePrototype(creatingList);
            if (b) { // 多例
                var message = buildCycleText(creatingList, context);
                throw new BeanCreationException("在类 " + context.beanClass().getName() + " 中, 检测到字段循环依赖（多例禁止），依赖链 = [" + message + "]");
            }
        }

        creatingList.add(context); // 加入正在创建列表
    }

    public static void endDependencyCheck() {
        var creatingList = CURRENTLY_CREATING.get();
        creatingList.removeLast();
    }

    public static String buildCycleText(List<DependentContext> creatingList, DependentContext beanClass) {
        // 如果已存在，组装一条依赖链
        var cycle = new ArrayList<String>();
        for (var creator : creatingList) {
            cycle.add(creator.beanClass().getName());
        }
        cycle.add(beanClass.beanClass().getName()); // 再加上自己形成完整回环
        return String.join(" -> ", cycle);
    }

    public static boolean checkContains(List<DependentContext> creatingList, DependentContext context) {
        for (var beanProvider : creatingList) {
            if (beanProvider.beanClass() == context.beanClass()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkAllArePrototype(List<DependentContext> creatingList) {
        for (var beanClass : creatingList) {
            if (beanClass.singleton()) {
                return false;
            }
        }
        return true;
    }

}
