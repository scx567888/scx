package cool.scx.config.test;

import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.source.ArgsConfigSource;
import cool.scx.config.source.JsonFileConfigSource;
import cool.scx.object.ScxObject;
import org.testng.annotations.Test;

public class ScxConfigTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var scxEnvironment = new ScxEnvironment(ScxConfigTest.class);
        var jsonPath = scxEnvironment.getPathByAppRoot("AppRoot:scx-config.json");
        var scxConfig = new ScxConfig(JsonFileConfigSource.of(jsonPath), ArgsConfigSource.of("--scx.port=8888"));
        var m = scxConfig.configMapping();
        System.out.println(ScxObject.toJson(m));
        System.out.println(scxConfig.get("scx.port"));
    }

}
