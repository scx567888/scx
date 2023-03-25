package cool.scx.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.dao.SchemaHelper;
import cool.scx.dao.Spy;
import cool.scx.dao.schema.CatalogMetaData;
import cool.scx.dao.schema.ColumnMetaData;
import cool.scx.dao.schema.SchemaMetaData;
import cool.scx.dao.schema.TableMetaData;
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

public class SchemaHelperTest {

    public static final Path TempSQLite;
    public static final String databaseName = "scx_dao_test";
    public static Path AppRoot;

    static {
        try {
            AppRoot = ClassUtils.getAppRoot(ClassUtils.getCodeSource(SchemaHelperTest.class));
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
        return Spy.wrap(mysqlDataSource);
    }

    public static DataSource getSQLiteDataSource() {
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        sqLiteDataSource.setUrl("jdbc:sqlite:" + TempSQLite);
        return Spy.wrap(sqLiteDataSource);
    }

    public static void main(String[] args) throws SQLException, JsonProcessingException {
        test1(getMySQLDataSource());
        test1(getSQLiteDataSource());
    }

    @Test
    public static void test1(DataSource dataSource) throws SQLException, JsonProcessingException {
        var dataBaseMetaData = SchemaHelper.getDataBaseMetaData(dataSource);
        var metaData = SchemaHelper.getTableMetaData(dataSource,"mysql",null,null,null);
        for (CatalogMetaData catalog : dataBaseMetaData.catalogs()) {
            System.out.println(catalog.catalogName() + " : ");
            System.out.println("{");
            for (SchemaMetaData schema : catalog.schemas()) {
                System.out.println("    " + schema.schemaName() + " : ");
                System.out.println("    {");
                for (TableMetaData table : schema.tables()) {
                    System.out.println("        " + table.tableName() + " : ");
                    System.out.println("        {");
                    for (ColumnMetaData column : table.columns()) {
                        System.out.println("            " + column.columnName() + " " + column.typeName() + "(" + column.columnSize() + ")" + (column.isNullable() ? " NULL" : " NOT NULL") + (column.isAutoincrement() ? " AUTOINCREMENT" : "") + " " + column.remarks());
                    }
                    for (var primaryKey : table.primaryKeys()) {
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
