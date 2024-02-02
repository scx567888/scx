package cool.scx.config.handler;

import cool.scx.config.ScxConfigValueHandler;
import cool.scx.util.CryptoUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ansi.Ansi;


public final class DecryptValueHandler implements ScxConfigValueHandler<String> {

    private final String password;

    private DecryptValueHandler(String password) {
        this.password = password;
    }

    public static DecryptValueHandler of(String password) {
        return new DecryptValueHandler(password);
    }

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
