package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.Test;

import static cool.scx.data.aggregation.AggregationBuilder.groupBy;
import static cool.scx.data.serialization.AggregationDeserializer.AGGREGATION_DEFINITION_DESERIALIZER;
import static cool.scx.data.serialization.AggregationSerializer.AGGREGATION_DEFINITION_SERIALIZER;

public class AggregationTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var aggregationDefinition = groupBy("name")
                .groupBy("reverseName", "REVERSE(name)")
                .agg("name", "SUM(name)");

        var json = AGGREGATION_DEFINITION_SERIALIZER.toJson(aggregationDefinition);
        var aggregationDefinition1 = AGGREGATION_DEFINITION_DESERIALIZER.fromJson(json);

        System.out.println(json);
        Assert.assertEquals(aggregationDefinition1.getAggs().length, 1);
        Assert.assertEquals(aggregationDefinition1.getGroupBys().length, 2);

    }

}
