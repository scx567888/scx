package cool.scx.common.util.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static cool.scx.common.util.ObjectUtils.*;

public class ObjectUtilsTest {

    @Test
    public static void test1() throws JsonProcessingException {
        var uu = new u("小明", null);
        Assert.assertEquals(toJson(uu), "{\"age\":null}");
        Assert.assertEquals(toJson(uu, new Options().setIgnoreNullValue(true)), "{}");
        Assert.assertEquals(toXml(uu), "<u><age/></u>");
        Assert.assertEquals(toJson(uu, new Options().setIgnoreJsonIgnore(true)), "{\"name\":\"小明\",\"age\":null}");
        Assert.assertEquals(toJson("justString"), "\"justString\"");

        var m = new HashMap<>();
        m.put("age", null);
        var m1 = new HashMap<>();
        m1.put("name", "小明");
        m1.put("age", null);
        Assert.assertEquals(convertValue(uu, ObjectUtils.MAP_TYPE), m);
        Assert.assertEquals(convertValue(uu, ObjectUtils.MAP_TYPE, new Options().setIgnoreJsonIgnore(true)), m1);
        Assert.assertEquals(convertValue(uu, ObjectUtils.MAP_TYPE, new Options().setIgnoreNullValue(true)), Map.of());
    }

    public static void main(String[] args) throws JsonProcessingException {
        test1();
    }

    public static class u {
        @JsonIgnore
        public String name;
        public Integer age;

        public u(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public u() {
        }
    }

}
