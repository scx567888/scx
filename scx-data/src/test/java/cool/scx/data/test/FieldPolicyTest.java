package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.field_policy.FieldPolicyBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import static cool.scx.data.field_policy.serializer.FieldPolicyDeserializer.FIELD_FILTER_DESERIALIZER;
import static cool.scx.data.field_policy.serializer.FieldPolicySerializer.FIELD_FILTER_SERIALIZER;

public class FieldPolicyTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var fieldPolicy = FieldPolicyBuilder
                .ofExcluded("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .ignoreNullValue(false)
                .addFieldExpression("w", "w * 2");
        var serialize = FIELD_FILTER_SERIALIZER.serialize(fieldPolicy);
        var json = ObjectUtils.toJson(serialize);
        var jsonNode = ObjectUtils.jsonMapper().readTree(json);
        var newFieldPolicy = (FieldPolicy) FIELD_FILTER_DESERIALIZER.deserialize(jsonNode);
        Assert.assertEquals(fieldPolicy.getFieldNames(), newFieldPolicy.getFieldNames());
        Assert.assertEquals(fieldPolicy.getFilterMode(), newFieldPolicy.getFilterMode());
        Assert.assertEquals(fieldPolicy.getIgnoreNullValue(), newFieldPolicy.getIgnoreNullValue());
        Assert.assertEquals(fieldPolicy.getFieldExpressions(), newFieldPolicy.getFieldExpressions());
    }

}
