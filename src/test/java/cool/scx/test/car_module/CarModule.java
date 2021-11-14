package cool.scx.test.car_module;

import cool.scx.ScxModule;

/**
 * <p>TestModule class.</p>
 *
 * @author scx567888
 * @version 1.3.14
 * @since 1.3.14
 */
public class CarModule implements ScxModule {

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        System.out.println("CarModule-Start");
    }

}