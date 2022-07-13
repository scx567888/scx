package cool.scx.config.handler;

import cool.scx.functional.ScxHandlerAR;
import cool.scx.tuple.KeyValue;
import cool.scx.util.CryptoUtils;
import cool.scx.util.ansi.Ansi;

/**
 * a
 */
public record DecryptValueHandler(String password) implements ScxHandlerAR<KeyValue<String, Object>, String> {

    @Override
    public String handle(KeyValue<String, Object> o) {
        var str = new ConvertValueHandler<>(String.class).handle(o);
        if (str != null) {
            try {
                return str.startsWith("DECRYPT:") ? CryptoUtils.decryptText(str.substring("DECRYPT:".length()), password) : str;
            } catch (Exception e) {
                Ansi.out().red("N 解密 " + o.key() + " 出错 !!!").println();
            }
        }
        return null;
    }

}
