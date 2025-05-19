package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import static cool.scx.data.aggregation.AggregationBuilder.groupBy;
import static cool.scx.data.aggregation.GroupByOption.USE_JSON_EXTRACT;
import static cool.scx.data.aggregation.serializer.AggregationDeserializer.AGGREGATION_DEFINITION_DESERIALIZER;
import static cool.scx.data.aggregation.serializer.AggregationSerializer.AGGREGATION_DEFINITION_SERIALIZER;

public class AggregationDefinitionTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var aggregationDefinition = groupBy("name", USE_JSON_EXTRACT).agg("name", "SUM(name)");

        var json = AGGREGATION_DEFINITION_SERIALIZER.toJson(aggregationDefinition);
        var aggregationDefinition1 = AGGREGATION_DEFINITION_DESERIALIZER.fromJson(json);

        System.out.println(json);
        Assert.assertEquals(aggregationDefinition.aggs(), aggregationDefinition1.aggs());

    }

}
