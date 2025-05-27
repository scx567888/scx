package cool.scx.data.test;

import cool.scx.data.aggregation.*;
import cool.scx.data.serialization.AggregationDeserializer;
import cool.scx.data.serialization.AggregationSerializer;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class AggregationSerializationTest {

    public static void main(String[] args) throws Exception {
        testAggregationSerializeDeserialize();
    }

    @Test
    public static void testAggregationSerializeDeserialize() throws Exception {
        // 构造Aggregation对象
        var aggregation = AggregationBuilder.aggregation()
                .groupBy("field1")
                .groupBy("aliasExpr", "SUM(amount)")
                .agg("totalAmount", "SUM(amount)")
                .agg("countRows", "COUNT(*)");

        // 序列化
        String json = AggregationSerializer.serializeAggregationToJson(aggregation);
        assertNotNull(json);
        assertTrue(json.contains("\"@type\":\"Aggregation\""));

        // 反序列化
        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json);
        assertNotNull(deserialized);

        // 验证groupBys
        GroupBy[] groupBys = deserialized.getGroupBys();
        assertEquals(groupBys.length, 2);

        assertTrue(groupBys[0] instanceof FieldGroupBy);
        assertEquals(((FieldGroupBy) groupBys[0]).fieldName(), "field1");

        assertTrue(groupBys[1] instanceof ExpressionGroupBy);
        ExpressionGroupBy expGroupBy = (ExpressionGroupBy) groupBys[1];
        assertEquals(expGroupBy.alias(), "aliasExpr");
        assertEquals(expGroupBy.expression(), "SUM(amount)");

        // 验证aggs
        Agg[] aggs = deserialized.getAggs();
        assertEquals(aggs.length, 2);
        assertEquals(aggs[0].alias(), "totalAmount");
        assertEquals(aggs[0].expression(), "SUM(amount)");
        assertEquals(aggs[1].alias(), "countRows");
        assertEquals(aggs[1].expression(), "COUNT(*)");
    }

}
