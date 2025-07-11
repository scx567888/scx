package cool.scx.object.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.scx.object.ScxObject;
import cool.scx.object.node.BigIntegerNode;
import cool.scx.object.node.Node;
import cool.scx.reflect.ScxReflect;
import cool.scx.reflect.TypeReference;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class ccc {


    public static void main(String[] args) throws IOException {

        var n = new nnn<Integer>();
        n.age = 99;
        n.name = "小明";
        n.cccc = new int[]{1, 2, 3};
        n.cccc1 = List.of(new Object[]{1, 2, 3, "呵呵呵"});
        n.cccc2 = List.of("字符串1","字符串2","字符串3");
        n.cccc3 = List.of(88,99,100);
        n.cccc4 = Map.of("value1",123,"value2",456);
        n.cccc5 = Map.of(889,123,998,456);

        var json = ScxObject.toJson(n);

        var o = ScxObject.fromJson(json, new TypeReference<nnn<BigInteger>>() {});

        System.out.println();

        ObjectMapper mapper = new ObjectMapper();

//        JsonNode jsonNode = mapper.readTree(json);

        var jjj = mapper.convertValue(o, JsonNode.class);
        System.out.println(jjj);

        

//        var s3 = ScxObject.toXml(node);
//        Node node1 = ScxObject.fromXml(s3);
//        System.out.println(ScxObject.toXml(node1));

//        n.h=n;


        String json1 = ScxObject.toJson(n);
//        var aaaNode = mapper.readTree(json1);
        var bbbNode = ScxObject.fromJson(json1);

//        var aaa = mapper.convertValue(aaaNode,nnn.class);
        var bbb = ScxObject.convertValue(bbbNode, nnn.class);
        System.out.println(bbb);
    }


    public static class fff {
        public String hhh = "从雕塑菜单";
        public String name = "nnnnnn";
        public Object h = "nnnnnn";
//        public int age;
//        public int[] cccc;
//        public List<Object> cccc1;

    }

    public static class nnn<T> extends fff {
        public String name;
        public int age;
        public int[] cccc;
        public List<Object> cccc1;
        public List<String> cccc2;
        public List<T> cccc3;
        public Map<String,T> cccc4;
        public Map<T,T> cccc5;

    }

    public static class jjj {
        public String name;
        public int age;
        public String cccc;
    }

}
