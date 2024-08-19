package cool.scx.ext.test;

import cool.scx.core.ScxModule;
import cool.scx.core.Scx;
import cool.scx.core.enumeration.ScxCoreFeature;
import cool.scx.ext.crud.CRUDModule;
import cool.scx.ext.fix_table.FixTableModule;
import cool.scx.ext.fss.FSSModule;
import cool.scx.ext.redirect.RedirectModule;
import cool.scx.ext.static_server.StaticServerModule;
import org.testng.annotations.Test;

public class ExtModuleTest extends ScxModule {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        Scx.builder()
                .setMainClass(ExtModuleTest.class)
                .addModule(
                        new ExtModuleTest(),
                        new CRUDModule(),
                        new FixTableModule(),
                        new FSSModule(),
                        new StaticServerModule(),
                        new RedirectModule()
                )
                .configure(ScxCoreFeature.USE_DEVELOPMENT_ERROR_PAGE, true)
                .configure(ScxCoreFeature.USE_SPY, true)
                .run();
    }

    @Override
    public void start(Scx scx) {
        scx.fixTable();
    }

}
