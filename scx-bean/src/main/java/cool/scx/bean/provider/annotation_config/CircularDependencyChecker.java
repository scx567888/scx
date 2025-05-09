package cool.scx.bean.provider.annotation_config;

import cool.scx.bean.exception.BeanCreationException;

import java.util.ArrayList;
import java.util.List;

/// 循环依赖检查器 todo 循环依赖输出待美化
class CircularDependencyChecker {

    /// 保存依赖链路
    private static final ThreadLocal<List<DependentContext>> CURRENTLY_CREATING = ThreadLocal.withInitial(ArrayList::new);

    public static void startDependencyCheck(DependentContext dependentContext) {
        // 获取当前的依赖链
        var creatingList = CURRENTLY_CREATING.get();

        // 检查依赖冲突
        var b = checkContains(creatingList, dependentContext);
        if (b) {
            // 如果当前构建的类的类型在依赖链中，说明发生了循环依赖
            var message = buildCycleText(creatingList, dependentContext);
            throw new BeanCreationException("在创建类 " + dependentContext.beanClass() + "时, 检测到构造函数参数循环依赖，依赖链 = [" + message + "]");
        }

        // 4. 将当前参数添加到依赖链中
        creatingList.add(dependentContext);

    }

    public static void endDependencyCheck() {
        var creatingList = CURRENTLY_CREATING.get();
        creatingList.removeLast();
    }

    private static boolean checkContains(List<DependentContext> creatingList, DependentContext dependentContext) {
        //检查标准 : 当前正在创建的 class 已经再链路中存在
        for (var beanProvider : creatingList) {
            if (beanProvider.beanClass() == dependentContext.beanClass()) {
                return true;
            }
        }
        return false;
    }

    private static String buildCycleText(List<DependentContext> creatingList, DependentContext dependentContext) {
        // 如果已存在，组装一条依赖链
        var cycle = new ArrayList<String>();
        for (var creator : creatingList) {
            cycle.add(creator.beanClass().getName());
        }
        cycle.add(dependentContext.beanClass().getName()); // 再加上自己形成完整回环
        return String.join(" -> ", cycle);
    }
    
}
