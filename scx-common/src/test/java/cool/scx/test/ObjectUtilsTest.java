package cool.scx.test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.util.ObjectUtils;
import org.testng.annotations.Test;

import static cool.scx.util.ObjectUtils.*;

public class ObjectUtilsTest {

    @Test
    public static void test1() throws JsonProcessingException {
        var uu = new u("司昌旭", null);
        System.out.println(toJson(uu));
        System.out.println(toJson(uu, ObjectUtils.Option.IGNORE_NULL_VALUE));
        System.out.println(toXml(uu));
        System.out.println(toJson(uu, ObjectUtils.Option.IGNORE_JSON_IGNORE));
        System.out.println(toJson("123123123"));

        System.out.println(convertValue(uu, ObjectUtils.MAP_TYPE));
        System.out.println(convertValue(uu, ObjectUtils.MAP_TYPE, Option.IGNORE_JSON_IGNORE));
        System.out.println(convertValue(uu, ObjectUtils.MAP_TYPE, Option.IGNORE_NULL_VALUE));
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
