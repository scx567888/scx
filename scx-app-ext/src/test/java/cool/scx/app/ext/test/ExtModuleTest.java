package cool.scx.app.ext.test;

import cool.scx.app.Scx;
import cool.scx.app.ScxModule;
import cool.scx.app.enumeration.ScxAppFeature;
import cool.scx.app.ext.crud.CRUDModule;
import cool.scx.app.ext.fix_table.FixTableModule;
import cool.scx.app.ext.fss.FSSModule;
import cool.scx.app.ext.redirect.RedirectModule;
import cool.scx.app.ext.static_server.StaticServerModule;
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
                .configure(ScxAppFeature.USE_DEVELOPMENT_ERROR_PAGE, true)
                .configure(ScxAppFeature.USE_SPY, true)
                .run();
    }

    @Override
    public void start(Scx scx) {
        scx.fixTable();
    }

}