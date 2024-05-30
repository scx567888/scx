package cool.scx.config.test;

import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.source.JsonFileConfigSource;
import org.testng.annotations.Test;

public class ScxConfigTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var scxEnvironment = new ScxEnvironment(ScxConfigTest.class);
        var jsonPath = scxEnvironment.getPathByAppRoot("AppRoot:scx-config.json");
        var scxConfig = new ScxConfig(JsonFileConfigSource.of(jsonPath));
        var m = scxConfig.configMapping();
        System.out.println(m.toPrettyString());
        System.out.println(scxConfig.get("scx.port"));
    }

}
