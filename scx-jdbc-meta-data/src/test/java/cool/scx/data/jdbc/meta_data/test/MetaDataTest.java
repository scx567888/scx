package cool.scx.data.jdbc.meta_data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.data.jdbc.meta_data.DataSourceMetaData;
import cool.scx.util.reflect.ClassUtils;
import org.sqlite.SQLiteDataSource;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import static com.mysql.cj.conf.PropertyKey.*;

public class MetaDataTest {

    public static final String databaseName = "scx_sql_test";
    public static final Path TempSQLite;
    public static Path AppRoot;

    static {
        try {
            AppRoot = ClassUtils.getAppRoot(ClassUtils.getCodeSource(MetaDataTest.class));
            TempSQLite = AppRoot.resolve("temp").resolve("temp.sqlite");
            Files.createDirectories(TempSQLite.getParent());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource getMySQLDataSource() {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setPort(3306);
        mysqlDataSource.setDatabaseName(databaseName);
        // 设置参数值
        mysqlDataSource.getProperty(allowMultiQueries).setValue(true);
        mysqlDataSource.getProperty(rewriteBatchedStatements).setValue(true);
        mysqlDataSource.getProperty(createDatabaseIfNotExist).setValue(true);
        return mysqlDataSource;
    }


    public static DataSource getSQLiteDataSource() {
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl("jdbc:sqlite:" + TempSQLite);
        return sqLiteDataSource;
    }

    public static void main(String[] args) throws SQLException, JsonProcessingException {
        test1();
    }

    @Test
    public static void test1() throws SQLException, JsonProcessingException {
        test1_1(getMySQLDataSource());
        test1_2(getMySQLDataSource());
        test1_1(getSQLiteDataSource());
        test1_2(getSQLiteDataSource());
    }

    public static void test1_1(DataSource dataSource) throws SQLException, JsonProcessingException {
        System.out.println("完全内省 !!!");
        try (var con = dataSource.getConnection()) {
            var dataSourceMetaData = new DataSourceMetaData().refreshCatalogs(con, true);
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
                            System.out.println("            " + column.name() + " " + column.typeName() + "(" + column.columnSize() + ")" + (column.notNull() ? " NOT NULL" : " NULL") + (column.autoIncrement() ? " AUTOINCREMENT" : "") + " DEFAULT : " + column.defaultValue() + ", REMARKS : " + column.remarks());
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


    public static void test1_2(DataSource dataSource) throws SQLException, JsonProcessingException {
        System.out.println("按需内省 !!!");
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
                        for (var column : table.refreshColumns(con).columns()) {
                            System.out.println("            " + column.name() + " " + column.typeName() + "(" + column.columnSize() + ")" + (column.notNull() ? " NOT NULL" : " NULL") + (column.autoIncrement() ? " AUTOINCREMENT" : "") + " DEFAULT : " + column.defaultValue() + ", REMARKS : " + column.remarks());
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
