package cool.scx.common.util.tree.test;

import cool.scx.common.util.tree.ScxTreeModel;
import cool.scx.common.util.tree.ScxTreeUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ScxTreeTest {

    public static void main(String[] args) throws Exception {
        test1();
    }

    @Test
    public static void test1() throws Exception {
        var l = new ArrayList<MyTree>();
        l.add(new MyTree(1, null, "根节点"));
        l.add(new MyTree(2, 1, "子节点1"));
        l.add(new MyTree(3, 1, "子节点2"));
        l.add(new MyTree(4, 3, "子节点3"));
        l.add(new MyTree(5, 3, "子节点4"));
        l.add(new MyTree(6, 1, "子节点5"));
        l.add(new MyTree(7, 2, "子节点6"));
        l.add(new MyTree(8, 2, "子节点7"));
        var myTrees = ScxTreeUtils.listToTree(l);
        var m = new HashMap<String, MyTree>();
        for (var myTree : myTrees) {
            ScxTreeUtils.walk(myTree, (parents, self) -> {
                var fullPath = getFullPath(parents, self);
                m.put(fullPath, self);
            });
        }
        var sb = new StringBuilder();
        m.forEach((k, v) -> sb.append(k).append("  -----  ").append(v.name).append("\n"));
        var s = """
                根节点/子节点2/子节点3  -----  子节点3
                根节点/子节点2/子节点4  -----  子节点4
                根节点  -----  根节点
                根节点/子节点5  -----  子节点5
                根节点/子节点1  -----  子节点1
                根节点/子节点1/子节点7  -----  子节点7
                根节点/子节点1/子节点6  -----  子节点6
                根节点/子节点2  -----  子节点2
                """;
        Assert.assertEquals(sb.toString(), s);
    }


    private static String getFullPath(List<MyTree> abstractVirtualFiles1, MyTree... abstractVirtualFiles2) {
        var fullList = new ArrayList<MyTree>();
        if (abstractVirtualFiles1 != null) {
            fullList.addAll(abstractVirtualFiles1);
        }
        Collections.addAll(fullList, abstractVirtualFiles2);
        return fullList.stream().map((f) -> f.name).collect(Collectors.joining("/"));
    }

    static class MyTree implements ScxTreeModel<MyTree> {
        Integer id;
        Integer parentID;
        List<MyTree> children;
        String name;


        public MyTree(Integer id, Integer parentID, String name) {
            this.id = id;
            this.parentID = parentID;
            this.name = name;
        }

        @Override
        public List<MyTree> children() {
            return children;
        }

        @Override
        public Object id() {
            return id;
        }

        @Override
        public Object parentID() {
            return parentID;
        }

        @Override
        public void children(List<MyTree> list) {
            this.children = list;
        }
    }

}
