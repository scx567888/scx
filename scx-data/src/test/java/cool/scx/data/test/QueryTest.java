package cool.scx.data.test;

import java.util.Arrays;

import static cool.scx.data.Query.query;
import static cool.scx.data.query.WhereBody.eq;

public class QueryTest {

    public static void main(String[] args) {
        var query = query().where(eq("name", "abc"));
        System.out.println(Arrays.toString(query.getWhere().clauses()));
    }

}
