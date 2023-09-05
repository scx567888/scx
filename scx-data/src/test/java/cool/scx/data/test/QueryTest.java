package cool.scx.data.test;

import java.util.Arrays;

import static cool.scx.data.QueryBuilder.eq;
import static cool.scx.data.QueryBuilder.query;

public class QueryTest {

    public static void main(String[] args) {
        var query = query().where(eq("name", "abc"));
        System.out.println(Arrays.toString(query.getWhere().clauses()));
    }

}
