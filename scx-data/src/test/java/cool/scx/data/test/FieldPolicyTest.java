package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static cool.scx.data.field_policy.FieldPolicyBuilder.exclude;
import static cool.scx.data.field_policy.serializer.FieldPolicyDeserializer.FIELD_POLICY_DESERIALIZER;
import static cool.scx.data.field_policy.serializer.FieldPolicySerializer.FIELD_POLICY_SERIALIZER;

public class FieldPolicyTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var fieldPolicy = exclude("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .ignoreNull(false)
                .ignoreNull("name", true)
                .expression("w", "w * 2");
        var json = FIELD_POLICY_SERIALIZER.toJson(fieldPolicy);
        var newFieldPolicy = FIELD_POLICY_DESERIALIZER.fromJson(json);
        Assert.assertEquals(fieldPolicy.fieldNames(), newFieldPolicy.fieldNames());
        Assert.assertEquals(fieldPolicy.filterMode(), newFieldPolicy.filterMode());
        Assert.assertEquals(fieldPolicy.ignoreNull(), newFieldPolicy.ignoreNull());
        Assert.assertEquals(fieldPolicy.ignoreNulls(), newFieldPolicy.ignoreNulls());
        Assert.assertEquals(fieldPolicy.expressions(), newFieldPolicy.expressions());
    }

}
