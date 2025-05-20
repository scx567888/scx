package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static cool.scx.data.field_policy.FieldPolicyBuilder.updateExclude;
import static cool.scx.data.field_policy.serializer.UpdateFieldPolicyDeserializer.UPDATE_FIELD_POLICY_DESERIALIZER;
import static cool.scx.data.field_policy.serializer.UpdateFieldPolicySerializer.UPDATE_FIELD_POLICY_SERIALIZER;

public class FieldPolicyTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
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
        Assert.assertEquals(fieldPolicy.getExpressions(), newFieldPolicy.getExpressions());
    }

}
