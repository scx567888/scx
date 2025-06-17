package cool.scx.common.tree;

import cool.scx.collections.multi_map.MultiMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/// ScxTreeUtils
///
/// @author scx567888
/// @version 0.0.1
public final class ScxTreeHelper {

    public static <T extends ScxTree<T>> void walk(final T scxTree, final ScxTreeVisitor<T> visitor) {
        _walk(null, scxTree, visitor);
    }

    private static <T extends ScxTree<T>> void _walk(final List<T> parents, final T currentScxTree, final ScxTreeVisitor<T> visitor) {
        visitor.handle(parents, currentScxTree);
        var newParents = parents == null ? new ArrayList<T>() : new ArrayList<>(parents);
        newParents.add(currentScxTree);
        if (currentScxTree.children() != null) {
            for (var child : currentScxTree.children()) {
                _walk(newParents, child, visitor);
            }
        }
    }

    /// 将 list 类型数据转换为 树形结构 (默认忽略孤儿节点)
    ///
    /// @param list 原始 list
    /// @param <T>  T
    /// @return 树形结构
    public static <T extends ScxTreeModel<T>> List<T> listToTree(List<T> list) {
        return listToTree(list, false);
    }

    /// 将 list 类型数据转换为 树形结构
    ///
    /// @param list          原始 list
    /// @param ignoreOrphans 是否忽略孤儿节点
    /// @param <T>           T
    /// @return 树形结构
    public static <T extends ScxTreeModel<T>> List<T> listToTree(List<T> list, boolean ignoreOrphans) {
        //数据合法性判断
        if (list == null) {
            throw new IllegalArgumentException("listToTree : 数据不能为空 !!!");
        }
        var idMap = new HashMap<>(list.size());
        var parentIDMap = new MultiMap<Object, T>();
        for (T t : list) {
            idMap.put(t.id(), t);
            parentIDMap.add(t.parentID(), t);
        }
        // 循环所有项, 并添加 children 属性
        return list.stream().filter(my -> {
            var myID = my.id(); //我自己的 id
            var parentID = my.parentID(); //我父亲的 id
            // 判断是否为孤儿 条件 1, 未启用忽略孤儿 flag && (1, 父节点为空 , 2, 没有任何节点的 ID 等于当前的父节点)
            var isOrphan = !ignoreOrphans && (parentID == null || idMap.get(parentID) == null);
            if (myID != null) {
                // 返回每一项的子级数组
                var myChildren = parentIDMap.getAll(myID);
                if (myChildren.size() > 0) {
                    my.children(myChildren);
                }
            }
            //需要返回的节点 1, 是根节点 2, 是孤儿节点
            return my.isRoot() || isOrphan;
        }).toList();

    }

}
