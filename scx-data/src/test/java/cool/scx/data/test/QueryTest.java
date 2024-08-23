package cool.scx.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.data.query.serializer.QuerySerializer;
import org.testng.annotations.Test;

import java.util.Arrays;

import static cool.scx.data.QueryBuilder.*;

public class QueryTest {

    public static void main(String[] args) throws JsonProcessingException {
        test1();
    }

    @Test
    public static void test1() throws JsonProcessingException {
        var and = and(eq("name", "abc"), eq("age", "10")).limit(1);
        var andSet = andSet().eq("abc", "123").limit(10).gt("bvd", "456");
        var parser = new TestWhereParser();
        var parse1 = parser.parse(and);
        var parse2 = parser.parse(andSet);
        System.out.println(parse1.whereClause() + " " + Arrays.toString(parse1.params()) + " LIMIT " + and.getLimit());
        System.out.println(parse2.whereClause() + " " + Arrays.toString(parse2.params()) + " LIMIT " + andSet.getLimit());
        
        var querySerializer = new QuerySerializer();
        String json = querySerializer.toJson(andSet);
        var andNew = querySerializer.fromJson(json);
        System.out.println(json);

    }

}
