package cool.scx.jdbc.test;

import cool.scx.jdbc.sql.NamedSQLListParameter;
import cool.scx.jdbc.sql.SQL;
import cool.scx.jdbc.sql.SQLBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static cool.scx.jdbc.dialect._default.DefaultDialect.DEFAULT_DIALECT;

public class SQLBuilderTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var a1 = "SELECT name, age FROM user WHERE age > ? GROUP BY name ORDER BY id LIMIT 10";
        var a2 = "DELETE FROM user WHERE age > ? ORDER BY id LIMIT 10";
        var a3 = "INSERT INTO user (name, age) VALUES (?, ?)";
        var a4 = "UPDATE user SET name = ? WHERE age > ? ORDER BY id LIMIT 10";

        var sql1 = SQLBuilder
                .Select("name", "age")
                .From("user")
                .Where("age > :age")
                .GroupBy("name")
                .OrderBy("id")
                .Limit(0L, 10L)
                .GetSQL(DEFAULT_DIALECT);

        var s1 = SQL.sqlNamed(sql1, Map.of("age", 18));

        var sql2 = SQLBuilder
                .Delete("user")
                .Where("age > ?")
                .OrderBy("id")
                .Limit(0L, 10L)
                .GetSQL(DEFAULT_DIALECT);

        var s2 = SQL.sql(sql2, 18);

        var sql3 = SQLBuilder
                .Insert("user", "name", "age")
                .Values(":name", ":age")
                .GetSQL(DEFAULT_DIALECT);

        var s3 = SQL.sqlNamed(sql3, List.of(
                Map.of("name", "小明", "age", 18),
                Map.of("name", "小红", "age", 20)
        ));

        var sql3_1 = SQLBuilder
                .Insert("user", "name", "age")
                .Values("?", "?")
                .GetSQL(DEFAULT_DIALECT);

        var s3_1 = SQL.sql(sql3_1, List.of(
                new Object[]{"小明", 18},
                new Object[]{"小红", 20}
        ));

        var sql4 = SQLBuilder
                .Update("user")
                .Set("name = :name")
                .Where("age > :age")
                .OrderBy("id")
                .Limit(10L)
                .GetSQL(DEFAULT_DIALECT);

        var s4 = SQL.sqlNamed(sql4, Map.of("name", "小明", "age", 18));

        Assert.assertEquals(s1.sql(), a1);
        Assert.assertEquals(s2.sql(), a2);
        Assert.assertEquals(s3.sql(), a3);
        Assert.assertEquals(s4.sql(), a4);
        Assert.assertEquals(s3.sql(), s3_1.sql());

        var sql5 = "select * from user where id in (:ids) and name = :name";

        //测试动态填充
        var a5 = "select * from user where id in (?, ?, ?, ?, ?) and name = ?";

        var s5 = SQL.sqlNamed(sql5, Map.of("ids", new NamedSQLListParameter(1, 2, 3, 4, 5), "name", "小明"));

        Assert.assertEquals(s5.sql(), a5);

        Assert.assertEquals(s5.params(), new Object[]{1, 2, 3, 4, 5, "小明"});

        var s51 = SQL.sqlNamed(sql5, Map.of("ids", new NamedSQLListParameter(List.of(1, 2, 3, 4, 5)), "name", "小明"));

        Assert.assertEquals(s51.sql(), a5);

        Assert.assertEquals(s51.params(), new Object[]{1, 2, 3, 4, 5, "小明"});

    }

}
