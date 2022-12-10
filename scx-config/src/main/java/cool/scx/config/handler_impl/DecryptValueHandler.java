package cool.scx.config.handler_impl;

import cool.scx.config.ScxConfigValueHandler;
import cool.scx.util.CryptoUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ansi.Ansi;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class DecryptValueHandler implements ScxConfigValueHandler<String> {

    private final String password;

    /**
     * <p>Constructor for DecryptValueHandler.</p>
     *
     * @param password a {@link java.lang.String} object
     */
    private DecryptValueHandler(String password) {
        this.password = password;
    }

    /**
     * <p>of.</p>
     *
     * @param password a {@link java.lang.String} object
     * @return a {@link cool.scx.config.handler_impl.DecryptValueHandler} object
     */
    public static DecryptValueHandler of(String password) {
        return new DecryptValueHandler(password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String handle(String keyPath, Object rawValue) {
        var str = ObjectUtils.convertValue(rawValue, String.class);
        if (str != null) {
            try {
                return str.startsWith("DECRYPT:") ? CryptoUtils.decryptText(str.substring("DECRYPT:".length()), password) : str;
            } catch (Exception e) {
                Ansi.out().red("N 解密 " + keyPath + " 出错 !!!").println();
            }
        }
        return null;
    }

}
