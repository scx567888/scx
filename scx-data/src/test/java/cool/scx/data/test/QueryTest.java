package cool.scx.data.test;

import cool.scx.data.Query;

public class QueryTest {

    public static void main(String[] args) {
        var query = new Query().equal("name", "abc");
        System.out.println(query.where().whereBodyList());
    }

}
