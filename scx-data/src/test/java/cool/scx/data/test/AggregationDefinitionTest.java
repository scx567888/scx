package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import static cool.scx.data.aggregation_definition.AggregationDefinitionBuilder.groupBy;
import static cool.scx.data.aggregation_definition.serializer.AggregationDefinitionDeserializer.AGGREGATION_DEFINITION_DESERIALIZER;
import static cool.scx.data.aggregation_definition.serializer.AggregationDefinitionSerializer.AGGREGATION_DEFINITION_SERIALIZER;

public class AggregationDefinitionTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var aggregationDefinition = groupBy("name").aggregateColumn("name", "SUM(name)");

        var serialize = AGGREGATION_DEFINITION_SERIALIZER.serializeAggregationDefinition(aggregationDefinition);
        var str = ObjectUtils.toJson(serialize, "");
        var jsonNode = ObjectUtils.jsonMapper().readTree(str);
        var aggregationDefinition1 = AGGREGATION_DEFINITION_DESERIALIZER.deserializeAggregationDefinition(jsonNode);

        Assert.assertEquals(aggregationDefinition.groupBys(), aggregationDefinition1.groupBys());
        Assert.assertEquals(aggregationDefinition.aggregateColumns(), aggregationDefinition1.aggregateColumns());

    }

}
