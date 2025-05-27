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
import static org.testng.AssertJUnit.assertNull;

public class QuerySerializationTest {

    public static void main(String[] args) throws Exception {
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
        testNestedWithBuildControl();
        testWhereClauseWithMultipleParams();
        testInConditionMultipleValues();
        testNotLikeCondition();
        testOrderBySameFieldDifferentOrder();
        testIsNullCondition();
        testIsNotNullCondition();
        testLongFieldNameCondition();
        testManyOrderBys();
        testFullQueryAllFields();
        testComplexNestedBuildControlMix();
        testInConditionWithMultipleValues();
        testNotWithLikeCondition();
        testSameFieldMultipleOrderBy();
        testNullAndNonNullConditionsMix();
        testLongFieldNames();
        testMultiOrderBys();
        testAllFieldsCombinedQuery();
        testNotLikeRegexWithControl();
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

    @Test
    public static void testNestedWithBuildControl() throws Exception {
        var condition = not(
                and(
                        eq("a", null, BuildControl.SKIP_IF_NULL),
                        or(
                                eq("b", 2, BuildControl.USE_EXPRESSION),
                                gt("c", 3)
                        )
                )
        );
        Query original = query().where(condition);
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(original.getWhere().getClass(), deserialized.getWhere().getClass());
        var notCondition = (Not) deserialized.getWhere();
        var andCondition = (And) notCondition.clause();
        assertEquals(2, andCondition.clauses().length);
    }

    @Test
    public static void testWhereClauseWithMultipleParams() throws Exception {
        Query original = query().where(whereClause("a = ? AND b = ?", 1, "test"));
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        var clause = (WhereClause) deserialized.getWhere();
        assertEquals("a = ? AND b = ?", clause.expression());
        assertEquals(2, clause.params().length);
    }

    @Test
    public static void testInConditionMultipleValues() throws Exception {
        Query original = where(in("id", List.of(1, 2, 3), BuildControl.USE_EXPRESSION));
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        var cond = (Condition) deserialized.getWhere();
        assertEquals("id", cond.selector());
        assertEquals(List.of(1, 2, 3), cond.value1());
        assertEquals(true, cond.useExpression());
    }

    @Test
    public static void testNotLikeCondition() throws Exception {
        Query original = where(not(like("name", "%admin%")));
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(Not.class, deserialized.getWhere().getClass());
        assertEquals(Condition.class, ((Not) deserialized.getWhere()).clause().getClass());
    }

    @Test
    public static void testOrderBySameFieldDifferentOrder() throws Exception {
        Query original = query().orderBys(
                asc("createdAt"),
                desc("createdAt")
        );
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(2, deserialized.getOrderBys().length);
        assertEquals("createdAt", deserialized.getOrderBys()[0].selector());
        assertEquals(OrderByType.ASC, deserialized.getOrderBys()[0].orderByType());
    }

    @Test
    public static void testIsNullCondition() throws Exception {
        Query original = where(eq("deletedAt", null));
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        var eq = (Condition) deserialized.getWhere();
        assertEquals("deletedAt", eq.selector());
        assertNull(eq.value1());
    }

    @Test
    public static void testIsNotNullCondition() throws Exception {
        Query original = where(not(eq("deletedAt", null)));
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(Not.class, deserialized.getWhere().getClass());
    }

    @Test
    public static void testLongFieldNameCondition() throws Exception {
        String longField = "user_profile_address_city_region_district_zipcode";
        Query original = where(eq(longField, "some-value"));
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        var eq = (Condition) deserialized.getWhere();
        assertEquals(longField, eq.selector());
    }

    @Test
    public static void testManyOrderBys() throws Exception {
        Query original = query().orderBys(
                asc("f1"), desc("f2"), asc("f3"), desc("f4"), asc("f5")
        );
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(5, deserialized.getOrderBys().length);
    }

    @Test
    public static void testFullQueryAllFields() throws Exception {
        Query original = query()
                .where(and(eq("a", 1), like("b", "%x%")))
                .orderBys(desc("updatedAt"))
                .offset(5L)
                .limit(10L);
        String json = QuerySerializer.serializeQueryToJson(original);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(5L, deserialized.getOffset().longValue());
        assertEquals(10L, deserialized.getLimit().longValue());
        assertEquals(1, deserialized.getOrderBys().length);
        assertEquals("updatedAt", deserialized.getOrderBys()[0].selector());
        assertEquals(And.class, deserialized.getWhere().getClass());
    }

    @Test
    public static void testComplexNestedBuildControlMix() throws SerializationException, DeserializationException {
        Where where = and(
                or(
                        eq("status", "active", BuildControl.SKIP_IF_NULL),
                        like("name", "%test%", BuildControl.USE_EXPRESSION)
                ),
                not(between("score", 60, 90))
        );
        Query query = query().where(where);
        String json = QuerySerializer.serializeQueryToJson(query);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(where.getClass(), deserialized.getWhere().getClass());
    }


    @Test
    public static void testInConditionWithMultipleValues() throws SerializationException, DeserializationException {
        Condition inCond = in("id", List.of(1, 2, 3, 4, 5), BuildControl.SKIP_IF_EMPTY_LIST);
        Query query = query().where(inCond);
        String json = QuerySerializer.serializeQueryToJson(query);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(inCond.getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testNotWithLikeCondition() throws SerializationException, DeserializationException {
        var notLike = not(like("name", "%admin%"));
        Query query = query().where(notLike);
        String json = QuerySerializer.serializeQueryToJson(query);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(notLike.getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testSameFieldMultipleOrderBy() throws SerializationException, DeserializationException {
        Query query = query().orderBys(asc("id"), desc("id"));
        String json = QuerySerializer.serializeQueryToJson(query);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(2, deserialized.getOrderBys().length);
        assertEquals("id", deserialized.getOrderBys()[0].selector());
        assertEquals(OrderByType.ASC, deserialized.getOrderBys()[0].orderByType());
        assertEquals(OrderByType.DESC, deserialized.getOrderBys()[1].orderByType());
    }

    @Test
    public static void testNullAndNonNullConditionsMix() throws SerializationException, DeserializationException {
        Query query = query().where(and(
                eq("deletedAt", null),
                eq("active", true)
        ));
        String json = QuerySerializer.serializeQueryToJson(query);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(query.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testLongFieldNames() throws SerializationException, DeserializationException {
        String longField = "some_really_long_and_complex_field_name_that_could_exist";
        Query query = query().where(eq(longField, 123));
        String json = QuerySerializer.serializeQueryToJson(query);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(query.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testMultiOrderBys() throws SerializationException, DeserializationException {
        Query query = query().orderBys(
                asc("age"),
                desc("createdAt"),
                asc("score"),
                desc("id")
        );
        String json = QuerySerializer.serializeQueryToJson(query);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(4, deserialized.getOrderBys().length);
    }

    @Test
    public static void testAllFieldsCombinedQuery() throws SerializationException, DeserializationException {
        Query query = query()
                .where(and(eq("status", "active"), gt("age", 18)))
                .orderBys(asc("name"), desc("createdAt"))
                .offset(5)
                .limit(15);
        String json = QuerySerializer.serializeQueryToJson(query);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(query.getOffset(), deserialized.getOffset());
        assertEquals(query.getLimit(), deserialized.getLimit());
        assertEquals(2, deserialized.getOrderBys().length);
        assertEquals(query.getWhere().getClass(), deserialized.getWhere().getClass());
    }

    @Test
    public static void testNotLikeRegexWithControl() throws SerializationException, DeserializationException {
        var cond = not(likeRegex("email", ".*@example.com", BuildControl.USE_EXPRESSION));
        Query query = query().where(cond);
        String json = QuerySerializer.serializeQueryToJson(query);
        Query deserialized = QueryDeserializer.deserializeQueryFromJson(json);
        assertEquals(cond.getClass(), deserialized.getWhere().getClass());
    }

}
