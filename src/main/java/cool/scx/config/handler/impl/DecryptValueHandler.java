package cool.scx.config.handler.impl;

import cool.scx.ScxHandlerR;
import cool.scx.config.handler.ScxConfigHandlerParam;
import cool.scx.util.CryptoUtils;
import cool.scx.util.ansi.Ansi;

public record DecryptValueHandler(String password) implements ScxHandlerR<ScxConfigHandlerParam, String> {

    @Override
    public String handle(ScxConfigHandlerParam o) {
        var str = new ConvertValueHandler<>(String.class).handle(o);
        if (str != null) {
            try {
                return str.startsWith("DECRYPT:") ? CryptoUtils.decryptText(str.substring("DECRYPT:".length()), password) : str;
            } catch (Exception e) {
                Ansi.out().red("N 解密 " + o.ketPath() + " 出错 !!!").println();
            }
        }
        return null;
    }

}
