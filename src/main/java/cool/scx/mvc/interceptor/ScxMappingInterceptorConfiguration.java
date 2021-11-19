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
     * 默认拦截器
     */
    private static final ScxMappingInterceptor defaultScxMappingInterceptor = new ScxMappingInterceptorImpl();

    /**
     * 拦截器
     */
    private static ScxMappingInterceptor scxMappingInterceptor = defaultScxMappingInterceptor;

    /**
     * a
     *
     * @param scxMappingInterceptor a
     */
    public static void setScxMappingInterceptor(ScxMappingInterceptor scxMappingInterceptor) {
        if (scxMappingInterceptor == null) {
            throw new IllegalArgumentException("ScxMappingInterceptor must not be empty !!!");
        }
        ScxMappingInterceptorConfiguration.scxMappingInterceptor = scxMappingInterceptor;
    }

    /**
     * a
     *
     * @return a
     */
    public static ScxMappingInterceptor interceptor() {
        return scxMappingInterceptor;
    }

}
