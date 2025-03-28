package cool.scx.http.headers.accept;

import static cool.scx.http.media_type.ScxMediaTypeHelper.SEMICOLON_PATTERN;

public class MediaRangeHelper {

    public static MediaRangeWritable parseMediaRange(String mediaRangeStr) throws IllegalMediaRangeException {
        var parts = SEMICOLON_PATTERN.split(mediaRangeStr);

        //处理意外情况
        if (parts.length == 0) {
            throw new IllegalMediaRangeException("Invalid media range: " + mediaRangeStr);
        }

        var mediaTypeStr = parts[0];
        var split = mediaTypeStr.split("/");
        if (split.length != 2) {
            throw new IllegalMediaRangeException("Invalid media type: " + mediaRangeStr);
        }

        var m = new MediaRangeImpl();

        m.type(split[0]);
        m.subtype(split[1]);

        for (var i = 1; i < parts.length; i = i + 1) {
            var s = parts[i].split("=", 2);
            if (s.length == 2) {
                m.params().add(s[0], s[1]);
            }
        }

        return m;

    }

}
