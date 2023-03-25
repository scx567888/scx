package cool.scx.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.cj.jdbc.MysqlDataSource;
import cool.scx.dao.SchemaHelper;
import cool.scx.dao.Spy;
import cool.scx.dao.schema.CatalogMetaData;
import cool.scx.dao.schema.ColumnMetaData;
import cool.scx.dao.schema.SchemaMetaData;
import cool.scx.dao.schema.TableMetaData;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.mysql.cj.conf.PropertyKey.*;

public class SchemaHelperTest {

    private static DataSource getMySQLDataSource() {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName("127.0.0.1");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setPort(3306);
        // 设置参数值
        mysqlDataSource.getProperty(allowMultiQueries).setValue(true);
        mysqlDataSource.getProperty(rewriteBatchedStatements).setValue(true);
        mysqlDataSource.getProperty(createDatabaseIfNotExist).setValue(true);
        return Spy.wrap(mysqlDataSource);
    }

    public static void main(String[] args) throws SQLException, JsonProcessingException {
        test1(getMySQLDataSource());
    }

    @Test
    public static void test1(DataSource dataSource) throws SQLException, JsonProcessingException {
        var dataBaseMetaData = SchemaHelper.getDataBaseMetaData(dataSource);
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
                        System.out.println("            " + column.columnName() + " " + column.typeName());
                    }
                    System.out.println("        }");
                }
                System.out.println("    }");
            }
            System.out.println("}");
        }
    }

}
