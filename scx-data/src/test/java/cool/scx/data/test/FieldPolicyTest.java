package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.FieldPolicy;
import org.testng.Assert;
import org.testng.annotations.Test;

import static cool.scx.data.field_policy.FieldPolicyBuilder.excluded;
import static cool.scx.data.field_policy.serializer.FieldPolicyDeserializer.FIELD_POLICY_DESERIALIZER;
import static cool.scx.data.field_policy.serializer.FieldPolicySerializer.FIELD_POLICY_SERIALIZER;

public class FieldPolicyTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var fieldPolicy = excluded("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .ignoreNullValue(false)
                .fieldExpression("w", "w * 2");
        var serialize = FIELD_POLICY_SERIALIZER.serialize(fieldPolicy);
        var json = ObjectUtils.toJson(serialize);
        var jsonNode = ObjectUtils.jsonMapper().readTree(json);
        var newFieldPolicy = (FieldPolicy) FIELD_POLICY_DESERIALIZER.deserialize(jsonNode);
        Assert.assertEquals(fieldPolicy.fieldNames(), newFieldPolicy.fieldNames());
        Assert.assertEquals(fieldPolicy.filterMode(), newFieldPolicy.filterMode());
        Assert.assertEquals(fieldPolicy.ignoreNullValue(), newFieldPolicy.ignoreNullValue());
        Assert.assertEquals(fieldPolicy.fieldExpressions(), newFieldPolicy.fieldExpressions());
    }

}
