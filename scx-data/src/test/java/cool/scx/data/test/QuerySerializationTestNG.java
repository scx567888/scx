package cool.scx.data.test;

import cool.scx.data.query.*;
import cool.scx.data.serialization.DeserializationException;
import cool.scx.data.serialization.QueryDeserializer;
import cool.scx.data.serialization.QuerySerializer;
import cool.scx.data.serialization.SerializationException;
import org.testng.annotations.Test;

import java.util.List;

import static cool.scx.data.query.QueryBuilder.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

public class QuerySerializationTestNG {

    public static void main(String[] args) throws SerializationException, DeserializationException {
        testSimpleConditionQuery();
        testComplexQuery();
        testQueryWithWhereClauseAndSkipIfInfo();
        testEmptyQuery();
        testQueryWithNullValueCondition();
        testExpressionQuery();
        testUseExpressionValueQuery();
        testDeeplyNestedQuery();
        testSkipIfInfoEmptyList();
        testBetweenCondition();
        testNotBetweenCondition();
        testOrderByAscDesc();
        testRegexCondition();
    }

    @Test
    public static void testSimpleConditionQuery() throws SerializationException, DeserializationException {
        Query original = where(eq("age", 18));

        String json = QuerySerializer.serializeQueryToJson(original);
        assertNotNull(json);
        System.out.println("Serialized JSON:\n" + json);

        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertNotNull(deserialized);

        assertEquals(original.getOffset(), deserialized.getOffset());
        assertEquals(original.getLimit(), deserialized.getLimit());
        assertEquals(original.getOrderBys().length, deserialized.getOrderBys().length);
        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testComplexQuery() throws SerializationException, DeserializationException {
        Condition cond1 = gt("age", 18);
        Condition cond2 = like("name", "%john%");
        Condition cond3 = eq("status", "active");
        Junction orClause = or(cond2, cond3);
        Junction andClause = and(cond1, orClause);

        OrderBy orderBy = desc("createdAt");

        Query original = query()
                .where(andClause)
                .orderBys(orderBy)
                .offset(10)
                .limit(50);

        String json = QuerySerializer.serializeQueryToJson(original);
        System.out.println("Serialized JSON:\n" + json);

        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertNotNull(deserialized);
        assertEquals((Long) 10L, deserialized.getOffset());
        assertEquals((Long) 50L, deserialized.getLimit());
        assertEquals(1, deserialized.getOrderBys().length);
        assertEquals(OrderByType.DESC, deserialized.getOrderBys()[0].orderByType());
        assertEquals(andClause.getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testQueryWithWhereClauseAndSkipIfInfo() throws SerializationException, DeserializationException {
        WhereClause whereClause = whereClause("field = ?", 123);
        Condition condition = eq("status", "active", BuildControl.SKIP_IF_NULL);

        Query original = query()
                .where(and(whereClause, condition));

        String json = QuerySerializer.serializeQueryToJson(original);
        System.out.println("Serialized JSON:\n" + json);

        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertNotNull(deserialized);

        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testEmptyQuery() throws SerializationException, DeserializationException {
        Query original = query();

        String json = QuerySerializer.serializeQueryToJson(original);
        assertNotNull(json);

        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(null, deserialized.getWhere());
        assertEquals(0, deserialized.getOrderBys().length);
        assertEquals(null, deserialized.getOffset());
        assertEquals(null, deserialized.getLimit());
    }

    @Test
    public static void testQueryWithNullValueCondition() throws SerializationException, DeserializationException {
        Query original = where(eq("deletedAt", null));

        String json = QuerySerializer.serializeQueryToJson(original);
        assertNotNull(json);

        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testExpressionQuery() throws SerializationException, DeserializationException {
        Query original = where(gt("score + bonus", 90, BuildControl.USE_EXPRESSION));

        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);

        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testUseExpressionValueQuery() throws SerializationException, DeserializationException {
        Query original = where(eq("score", "NOW()", BuildControl.USE_EXPRESSION_VALUE));

        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);

        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testDeeplyNestedQuery() throws SerializationException, DeserializationException {
        Where nested = and(
                or(
                        and(
                                eq("a", 1),
                                eq("b", 2)
                        ),
                        eq("c", 3)
                ),
                not(eq("d", 4))
        );

        Query original = query().where(nested);
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);

        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testSkipIfInfoEmptyList() throws SerializationException, DeserializationException {
        Query original = where(in("id", List.of(), BuildControl.SKIP_IF_EMPTY_LIST));

        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);

        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testBetweenCondition() throws SerializationException, DeserializationException {
        Query original = where(between("age", 18, 30));

        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);

        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testOrderByAscDesc() throws SerializationException, DeserializationException {
        Query original = query().orderBys(
                asc("age"),
                desc("createdAt")
        );

        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);

        assertEquals(2, deserialized.getOrderBys().length);
        assertEquals(OrderByType.ASC, deserialized.getOrderBys()[0].orderByType());
        assertEquals(OrderByType.DESC, deserialized.getOrderBys()[1].orderByType());
    }

    @Test
    public static void testRegexCondition() throws SerializationException, DeserializationException {
        Query original = where(likeRegex("name", "^A.*"));

        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);

        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testNotBetweenCondition() throws SerializationException, DeserializationException {
        Query original = where(notBetween("score", 60, 80));

        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);

        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
    }


}
