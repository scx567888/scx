package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static cool.scx.data.field_policy.FieldPolicyBuilder.exclude;
import static cool.scx.data.serialization.FieldPolicyDeserializer.FIELD_POLICY_DESERIALIZER;
import static cool.scx.data.serialization.FieldPolicySerializer.FIELD_POLICY_SERIALIZER;

public class FieldPolicyTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
        test2();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var fieldPolicy = exclude("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .ignoreNull(false)
                .ignoreNull("name", true)
                .assignField("w", "w * 2");
        var json = FIELD_POLICY_SERIALIZER.toJson(fieldPolicy);
        var newFieldPolicy = FIELD_POLICY_DESERIALIZER.fromJson(json);
        Assert.assertEquals(fieldPolicy.getFieldNames(), newFieldPolicy.getFieldNames());
        Assert.assertEquals(fieldPolicy.getFilterMode(), newFieldPolicy.getFilterMode());
        Assert.assertEquals(fieldPolicy.getIgnoreNull(), newFieldPolicy.getIgnoreNull());
        Assert.assertEquals(fieldPolicy.getIgnoreNulls(), newFieldPolicy.getIgnoreNulls());
        Assert.assertEquals(fieldPolicy.getAssignFields().length, newFieldPolicy.getAssignFields().length);
    }

    @Test
    public static void test2() throws JsonProcessingException {
        var fieldPolicy = exclude("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .virtualField("LENGTH(a)", "a");
        var json = FIELD_POLICY_SERIALIZER.toJson(fieldPolicy);
        var newFieldPolicy = FIELD_POLICY_DESERIALIZER.fromJson(json);
        Assert.assertEquals(fieldPolicy.getFieldNames(), newFieldPolicy.getFieldNames());
        Assert.assertEquals(fieldPolicy.getFilterMode(), newFieldPolicy.getFilterMode());
        Assert.assertEquals(fieldPolicy.getVirtualFields().length, newFieldPolicy.getVirtualFields().length);
    }

}
