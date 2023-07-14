package cool.scx.data.test;

import cool.scx.data.Query;

import java.util.Arrays;

import static cool.scx.data.query.WhereBody.equal;

public class QueryTest {

    public static void main(String[] args) {
        var query = new Query().where(equal("name", "abc"));
        System.out.println(Arrays.toString(query.getWhere().whereBodyList()));
    }

}
