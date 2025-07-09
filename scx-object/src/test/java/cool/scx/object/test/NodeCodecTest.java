package cool.scx.object.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cool.scx.object.ScxObject;
import org.testng.annotations.Test;

public class NodeCodecTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
        test2();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var json = """
                {
                 "a" : "小明",
                 "b" : 10000,
                 "c" : [1 ,2, 3, "哈哈哈"]
                }
                """;
        
        var node = ScxObject.fromJson(json);
        
        var xml = ScxObject.toXml(node);

        var node1 = ScxObject.fromXml(xml);

        var json1 = ScxObject.toJson(node1);

        System.out.println();
    }

    @Test
    public static void test2() throws JsonProcessingException {
        var jsonMapper=new JsonMapper();
        var xmlMapper=new XmlMapper();
        var json = """
                {
                 "a" : "小明",
                 "b" : 10000,
                 "c" : [1 ,2, 3, "哈哈哈"]
                }
                """;

        var node = jsonMapper.readTree(json);

        var xml = xmlMapper.writeValueAsString(node);

        var node1 = xmlMapper.readTree(xml);

        var json1 = jsonMapper.writeValueAsString(node1);

        System.out.println();
    }


}
