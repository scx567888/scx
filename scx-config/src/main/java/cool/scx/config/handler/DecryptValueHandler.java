package cool.scx.config.handler;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.ansi.Ansi;
import cool.scx.common.util.CryptoUtils;
import cool.scx.common.util.ObjectUtils;
import cool.scx.config.ScxConfigValueHandler;

/// DecryptValueHandler
///
/// @author scx567888
/// @version 0.0.1
public final class DecryptValueHandler implements ScxConfigValueHandler<String> {

    private final String password;

    private DecryptValueHandler(String password) {
        this.password = password;
    }

    public static DecryptValueHandler of(String password) {
        return new DecryptValueHandler(password);
    }

    @Override
    public String handle(String keyPath, JsonNode rawValue) {
        var str = ObjectUtils.convertValue(rawValue, String.class);
        if (str != null) {
            try {
                return str.startsWith("DECRYPT:") ? CryptoUtils.decryptText(str.substring("DECRYPT:".length()), password) : str;
            } catch (Exception e) {
                Ansi.ansi().red("N 解密 " + keyPath + " 出错 !!!").println();
            }
        }
        return null;
    }

}
