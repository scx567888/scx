package cool.scx.data.test;

import org.testng.annotations.Test;

import static cool.scx.data.query.QueryBuilder.eq;

public class DataTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var repo = new TestRepository<>();

        //方式1 
        var tc = new TestTransactionContext();

        repo.find(eq("id", 1), tc);
        repo.delete(eq("id", 1), tc);

        tc.commit();

        //方式2
        var m = new TestTransactionManager();
        m.withTransaction(tc1 -> {
            repo.find(eq("id", 1), tc1);
            repo.delete(eq("id", 1), tc1);
        });

        //方式3
        m.autoTransaction(() -> {
            repo.find(eq("id", 1));
            repo.delete(eq("id", 1));
        });

    }

}
