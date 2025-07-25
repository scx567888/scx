package cool.scx.jdbc.mysql.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.jdbc.meta_data.DataSourceMetaData;
import cool.scx.jdbc.mysql.MySQLDialect;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

public class MetaDataTest {

    public static void main(String[] args) throws SQLException {
        test1();
    }

    @Test
    public static void test1() throws SQLException {
        test1_1(SQLRunnerForMySQLTest.dataSource);
        test1_2(SQLRunnerForMySQLTest.dataSource);
    }

    public static void test1_1(DataSource dataSource) throws SQLException {
        System.out.println("完全内省 !!!");
        var dialect = new MySQLDialect();
        try (var con = dataSource.getConnection()) {
            var dataSourceMetaData = new DataSourceMetaData().refreshCatalogsDeep(con, dialect);
            for (var catalog : dataSourceMetaData.catalogs()) {
                System.out.println(catalog.name() + " : ");
                System.out.println("{");
                for (var schema : catalog.schemas()) {
                    System.out.println("    " + schema.name() + " : ");
                    System.out.println("    {");
                    for (var table : schema.tables()) {
                        System.out.println("        " + table.name() + " : ");
                        System.out.println("        {");
                        for (var column : table.columns()) {
                            System.out.println("            " + column.name() + " " + column.dataType().name() + "(" + column.dataType().length() + ")" + (column.notNull() ? " NOT NULL" : " NULL") + (column.autoIncrement() ? " AUTOINCREMENT" : "") + " DEFAULT : " + column.defaultValue() + ", REMARKS : " + column.comment());
                        }
                        for (var primaryKey : table.keys()) {
                            System.out.println("            " + primaryKey.columnName() + " primaryKeys ");
                        }
                        System.out.println("        }");
                    }
                    System.out.println("    }");
                }
                System.out.println("}");
            }
        }

    }


    public static void test1_2(DataSource dataSource) throws SQLException {
        System.out.println("按需内省 !!!");
        var dialect = new MySQLDialect();
        try (var con = dataSource.getConnection()) {
            var dataSourceMetaData = new DataSourceMetaData().refreshCatalogs(con);
            for (var catalog : dataSourceMetaData.catalogs()) {
                System.out.println(catalog.name() + " : ");
                System.out.println("{");
                for (var schema : catalog.refreshSchemas(con).schemas()) {
                    System.out.println("    " + schema.name() + " : ");
                    System.out.println("    {");
                    for (var table : schema.refreshTables(con).tables()) {
                        System.out.println("        " + table.name() + " : ");
                        System.out.println("        {");
                        for (var column : table.refreshColumns(con, dialect).columns()) {
                            System.out.println("            " + column.name() + " " + column.dataType().name() + "(" + column.dataType().length() + ")" + (column.notNull() ? " NOT NULL" : " NULL") + (column.autoIncrement() ? " AUTOINCREMENT" : "") + " DEFAULT : " + column.defaultValue() + ", REMARKS : " + column.comment());
                        }
                        for (var primaryKey : table.keys()) {
                            System.out.println("            " + primaryKey.columnName() + " primaryKeys ");
                        }
                        System.out.println("        }");
                    }
                    System.out.println("    }");
                }
                System.out.println("}");
            }
        }
    }

}
