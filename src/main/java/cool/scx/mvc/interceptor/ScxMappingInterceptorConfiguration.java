package cool.scx.mvc.interceptor;

import cool.scx.mvc.interceptor.impl.ScxMappingInterceptorImpl;

/**
 * <p>ScxMappingConfiguration class.</p>
 *
 * @author scx567888
 * @version 1.3.14
 */
public final class ScxMappingInterceptorConfiguration {

    /**
     * 拦截器
     */
    private static final ScxMappingInterceptor defaultScxMappingInterceptor = new ScxMappingInterceptorImpl();

    private static ScxMappingInterceptor scxMappingInterceptor = defaultScxMappingInterceptor;

    public static void setScxMappingInterceptor(ScxMappingInterceptor scxMappingInterceptor) {
        if (scxMappingInterceptor == null) {
            throw new IllegalArgumentException("ScxMappingInterceptor must not be empty !!!");
        }
        ScxMappingInterceptorConfiguration.scxMappingInterceptor = scxMappingInterceptor;
    }

    public static ScxMappingInterceptor interceptor() {
        return scxMappingInterceptor;
    }

}
