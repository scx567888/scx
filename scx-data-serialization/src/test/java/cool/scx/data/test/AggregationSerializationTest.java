package cool.scx.data.test;

import dev.scx.data.aggregation.*;
import cool.scx.data.serialization.AggregationDeserializer;
import cool.scx.data.serialization.AggregationSerializer;
import cool.scx.data.serialization.DeserializationException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class AggregationSerializationTest {

    public static void main(String[] args) throws Exception {
        testAggregationSerializeDeserialize();
        testOnlyGroupBy();
        testOnlyAggs();
        testEmptyAggregation();
        testMultipleFieldGroupBy();
        testMultipleExpressionGroupBy();
        testMultipleAggs();
        testComplexExpressions();
        testJsonRoundTrip();
        testDeserializeNullThrows();
        testDeserializeWrongTypeThrows();
    }

    @Test
    public static void testAggregationSerializeDeserialize() throws Exception {
        var aggregation = AggregationBuilder.aggregation()
                .groupBy("field1")
                .groupBy("aliasExpr", "SUM(amount)")
                .agg("totalAmount", "SUM(amount)")
                .agg("countRows", "COUNT(*)");

        String json = AggregationSerializer.serializeAggregationToJson(aggregation);
        assertNotNull(json);
        assertTrue(json.contains("\"@type\":\"Aggregation\""));

        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json);
        assertNotNull(deserialized);

        GroupBy[] groupBys = deserialized.getGroupBys();
        assertEquals(groupBys.length, 2);
        assertTrue(groupBys[0] instanceof FieldGroupBy);
        assertEquals(((FieldGroupBy) groupBys[0]).fieldName(), "field1");
        assertTrue(groupBys[1] instanceof ExpressionGroupBy);
        ExpressionGroupBy expGroupBy = (ExpressionGroupBy) groupBys[1];
        assertEquals(expGroupBy.alias(), "aliasExpr");
        assertEquals(expGroupBy.expression(), "SUM(amount)");

        Agg[] aggs = deserialized.getAggs();
        assertEquals(aggs.length, 2);
        assertEquals(aggs[0].alias(), "totalAmount");
        assertEquals(aggs[0].expression(), "SUM(amount)");
        assertEquals(aggs[1].alias(), "countRows");
        assertEquals(aggs[1].expression(), "COUNT(*)");
    }

    @Test
    public static void testOnlyGroupBy() throws Exception {
        var aggregation = AggregationBuilder.aggregation()
                .groupBy("onlyField");

        String json = AggregationSerializer.serializeAggregationToJson(aggregation);
        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json);

        assertEquals(1, deserialized.getGroupBys().length);
        assertEquals(0, deserialized.getAggs().length);
        assertEquals("onlyField", ((FieldGroupBy) deserialized.getGroupBys()[0]).fieldName());
    }

    @Test
    public static void testOnlyAggs() throws Exception {
        var aggregation = AggregationBuilder.aggregation()
                .agg("sumField", "SUM(field)");

        String json = AggregationSerializer.serializeAggregationToJson(aggregation);
        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json);

        assertEquals(0, deserialized.getGroupBys().length);
        assertEquals(1, deserialized.getAggs().length);
        assertEquals("sumField", deserialized.getAggs()[0].alias());
        assertEquals("SUM(field)", deserialized.getAggs()[0].expression());
    }

    @Test
    public static void testEmptyAggregation() throws Exception {
        var aggregation = AggregationBuilder.aggregation();

        String json = AggregationSerializer.serializeAggregationToJson(aggregation);
        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json);

        assertEquals(0, deserialized.getGroupBys().length);
        assertEquals(0, deserialized.getAggs().length);
    }

    @Test
    public static void testMultipleFieldGroupBy() throws Exception {
        var aggregation = AggregationBuilder.aggregation()
                .groupBy("f1")
                .groupBy("f2")
                .groupBy("f3");

        String json = AggregationSerializer.serializeAggregationToJson(aggregation);
        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json);

        assertEquals(3, deserialized.getGroupBys().length);
        assertEquals("f1", ((FieldGroupBy) deserialized.getGroupBys()[0]).fieldName());
        assertEquals("f2", ((FieldGroupBy) deserialized.getGroupBys()[1]).fieldName());
        assertEquals("f3", ((FieldGroupBy) deserialized.getGroupBys()[2]).fieldName());
    }

    @Test
    public static void testMultipleExpressionGroupBy() throws Exception {
        var aggregation = AggregationBuilder.aggregation()
                .groupBy("a1", "expr1")
                .groupBy("a2", "expr2");

        String json = AggregationSerializer.serializeAggregationToJson(aggregation);
        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json);

        assertEquals(2, deserialized.getGroupBys().length);
        assertEquals("a1", ((ExpressionGroupBy) deserialized.getGroupBys()[0]).alias());
        assertEquals("expr1", ((ExpressionGroupBy) deserialized.getGroupBys()[0]).expression());
        assertEquals("a2", ((ExpressionGroupBy) deserialized.getGroupBys()[1]).alias());
        assertEquals("expr2", ((ExpressionGroupBy) deserialized.getGroupBys()[1]).expression());
    }

    @Test
    public static void testMultipleAggs() throws Exception {
        var aggregation = AggregationBuilder.aggregation()
                .agg("a1", "expr1")
                .agg("a2", "expr2")
                .agg("a3", "expr3");

        String json = AggregationSerializer.serializeAggregationToJson(aggregation);
        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json);

        assertEquals(3, deserialized.getAggs().length);
        assertEquals("a1", deserialized.getAggs()[0].alias());
        assertEquals("expr1", deserialized.getAggs()[0].expression());
        assertEquals("a2", deserialized.getAggs()[1].alias());
        assertEquals("expr2", deserialized.getAggs()[1].expression());
        assertEquals("a3", deserialized.getAggs()[2].alias());
        assertEquals("expr3", deserialized.getAggs()[2].expression());
    }

    @Test
    public static void testComplexExpressions() throws Exception {
        var aggregation = AggregationBuilder.aggregation()
                .groupBy("complexAlias", "CASE WHEN amount > 0 THEN 'pos' ELSE 'neg' END")
                .agg("avgValue", "AVG(CASE WHEN status='A' THEN value ELSE 0 END)");

        String json = AggregationSerializer.serializeAggregationToJson(aggregation);
        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json);

        var groupBy = (ExpressionGroupBy) deserialized.getGroupBys()[0];
        assertEquals("complexAlias", groupBy.alias());
        assertTrue(groupBy.expression().contains("CASE WHEN"));

        var agg = deserialized.getAggs()[0];
        assertEquals("avgValue", agg.alias());
        assertTrue(agg.expression().contains("AVG("));
    }

    @Test
    public static void testJsonRoundTrip() throws Exception {
        var aggregation = AggregationBuilder.aggregation()
                .groupBy("fieldA")
                .agg("aggA", "COUNT(*)");

        String json1 = AggregationSerializer.serializeAggregationToJson(aggregation);
        Aggregation deserialized = AggregationDeserializer.deserializeAggregationFromJson(json1);
        String json2 = AggregationSerializer.serializeAggregationToJson(deserialized);

        assertEquals(json1, json2);
    }

    @Test
    public static void testDeserializeNullThrows() {
        assertThrows(DeserializationException.class, () -> {
            AggregationDeserializer.deserializeAggregationFromJson("null");
        });
    }

    @Test
    public static void testDeserializeWrongTypeThrows() {
        String json = "{\"@type\":\"WrongType\"}";
        assertThrows(DeserializationException.class, () -> {
            AggregationDeserializer.deserializeAggregationFromJson(json);
        });
    }

}
