package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static cool.scx.data.field_policy.FieldPolicyBuilder.queryExclude;
import static cool.scx.data.field_policy.FieldPolicyBuilder.updateExclude;
import static cool.scx.data.field_policy.serializer.QueryFieldPolicyDeserializer.QUERY_FIELD_POLICY_DESERIALIZER;
import static cool.scx.data.field_policy.serializer.QueryFieldPolicySerializer.QUERY_FIELD_POLICY_SERIALIZER;
import static cool.scx.data.field_policy.serializer.UpdateFieldPolicyDeserializer.UPDATE_FIELD_POLICY_DESERIALIZER;
import static cool.scx.data.field_policy.serializer.UpdateFieldPolicySerializer.UPDATE_FIELD_POLICY_SERIALIZER;

public class FieldPolicyTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
        test2();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var fieldPolicy = updateExclude("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .ignoreNull(false)
                .ignoreNull("name", true)
                .expression("w", "w * 2");
        var json = UPDATE_FIELD_POLICY_SERIALIZER.toJson(fieldPolicy);
        var newFieldPolicy = UPDATE_FIELD_POLICY_DESERIALIZER.fromJson(json);
        Assert.assertEquals(fieldPolicy.getFieldNames(), newFieldPolicy.getFieldNames());
        Assert.assertEquals(fieldPolicy.getFilterMode(), newFieldPolicy.getFilterMode());
        Assert.assertEquals(fieldPolicy.getIgnoreNull(), newFieldPolicy.getIgnoreNull());
        Assert.assertEquals(fieldPolicy.getIgnoreNulls(), newFieldPolicy.getIgnoreNulls());
        Assert.assertEquals(fieldPolicy.getExpressions().length, newFieldPolicy.getExpressions().length);
    }

    @Test
    public static void test2() throws JsonProcessingException {
        var fieldPolicy = queryExclude("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .virtualField("LENGTH(a)","a");
        var json = QUERY_FIELD_POLICY_SERIALIZER.toJson(fieldPolicy);
        var newFieldPolicy = QUERY_FIELD_POLICY_DESERIALIZER.fromJson(json);
        Assert.assertEquals(fieldPolicy.getFieldNames(), newFieldPolicy.getFieldNames());
        Assert.assertEquals(fieldPolicy.getFilterMode(), newFieldPolicy.getFilterMode());
        Assert.assertEquals(fieldPolicy.getVirtualFields().length, newFieldPolicy.getVirtualFields().length);
    }

}
