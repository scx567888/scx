package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;

/// AcceptHelper
///
/// @author scx567888
/// @version 0.0.1
public class AcceptHelper {

    public static AcceptElementWritable decodeAccept(String s) {
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
        return new AcceptElementImpl().mediaType(ScxMediaType.of(type)).q(weight);
    }

    public static AcceptWritable decodeAccepts(String s) {
        if (s == null) {
            return null;
        }
        var writable = new AcceptImpl();
        var mediaTypes = s.split(",");
        for (var mediaType : mediaTypes) {
            var a = decodeAccept(mediaType);
            writable.add(a);
        }
        return writable;
    }

}
