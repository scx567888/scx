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
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var q1 = and().eq("name", "小明").between("age", 1, 10).orderBys(desc("name"), desc("age"))
                .limit(10).offset(12);
        var q2 = and(
                or(
                        and(
                                eq("name", "小明")
                        )
                )
        ).orderBys(desc("name"), desc("age"))
                .limit(10).offset(12);
        String json = QUERY_SERIALIZER.toJson(q1);
        var andNew = QUERY_DESERIALIZER.fromJson(json);
        System.out.println(json);
        String json1 = QUERY_SERIALIZER.toJson(q2);
        var andNew2 = QUERY_DESERIALIZER.fromJson(json1);
        System.out.println(json1);

    }

}
