package cool.scx.data.test;

import java.util.Arrays;

import static cool.scx.data.Query.eq;
import static cool.scx.data.Query.query;

public class QueryTest {

    public static void main(String[] args) {
        var query = query().where(eq("name", "abc"));
        System.out.println(Arrays.toString(query.getWhere().clauses()));
    }

}
