package cool.scx.http.accept;

import cool.scx.http.ScxMediaType;

/**
 * AcceptHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
public class AcceptHelper {

    public static AcceptWritable decodeAccept(String s) {
        var parts = s.split(";");
        var type = parts[0].trim();
        double weight = 1.0; // 默认权重为1.0
        if (parts.length > 1) {
            for (String part : parts) {
                part = part.trim();
                if (part.startsWith("q=")) {
                    try {
                        weight = Double.parseDouble(part.substring(2));
                    } catch (NumberFormatException e) {
                        // 如果解析失败，保持默认权重
                    }
                }
            }
        }
        return new AcceptImpl().mediaType(ScxMediaType.of(type)).q(weight);
    }

    public static AcceptsWritable decodeAccepts(String s) {
        if (s == null) {
            return null;
        }
        var writable = new AcceptsImpl();
        var mediaTypes = s.split(",");
        for (var mediaType : mediaTypes) {
            var a = decodeAccept(mediaType);
            writable.add(a);
        }
        return writable;
    }

}
