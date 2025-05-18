package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static cool.scx.data.query.QueryBuilder.*;
import static cool.scx.data.query.serializer.QueryDeserializer.QUERY_DESERIALIZER;
import static cool.scx.data.query.serializer.QuerySerializer.QUERY_SERIALIZER;

public class QueryTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
        test2();
    }

    @Test
    public static void test1() {
        var and = and(eq("name", "abc"), eq("age", "10")).limit(1);
        var andSet = and().eq("abc", "123").limit(10).gt("bvd", "456");
        var parser = new TestWhereParser();
        var parse1 = parser.parse(and);
        var parse2 = parser.parse(andSet);
        System.out.println(parse1.whereClause() + " " + Arrays.toString(parse1.params()) + " LIMIT " + and.getLimit());
        System.out.println(parse2.whereClause() + " " + Arrays.toString(parse2.params()) + " LIMIT " + andSet.getLimit());

    }

    @Test
    public static void test2() throws JsonProcessingException {
        var q1 = and().eq("name", "小明").between("age", 1, 10).orderBy("desc name", desc("age"))
//                .groupBy("class", groupBy("abc")) todo
                .limit(10).offset(12);
        var q2 = and(
                or(
                        and(
                                eq("name", "小明")
                        )
                )
        ).orderBy("desc name", desc("age"))
//                .groupBy("class", groupBy("abc")) todo
                .limit(10).offset(12);
        String json = QUERY_SERIALIZER.toJson(q1);
        var andNew = QUERY_DESERIALIZER.fromJson(json);
        System.out.println(json);
        String json1 = QUERY_SERIALIZER.toJson(q2);
        var andNew2 = QUERY_DESERIALIZER.fromJson(json1);
        System.out.println(json1);

        var parser = new TestWhereParser();
        var parse1 = parser.parse(q1);
        var parse2 = parser.parse(andNew);
        Assert.assertEquals(parse1.whereClause(), parse2.whereClause());
        Assert.assertEquals(parse1.params(), parse2.params());

        var parse11 = parser.parse(q2);
        var parse21 = parser.parse(andNew2);
        Assert.assertEquals(parse11.whereClause(), parse21.whereClause());
        Assert.assertEquals(parse11.params(), parse21.params());
    }

}
