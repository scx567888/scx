package cool.scx.http.header.range;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.min;
import static java.lang.Long.parseLong;

/// HttpHeader Range
///
/// @param start 可以为空
/// @param end   可以为空
/// @author scx567888
/// @version 0.0.1
public record Range(Long start, Long end) {

    public static List<Range> parseRange(String rangeHeader) {
        List<Range> ranges = new ArrayList<>();
        if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
            return ranges;
        }

        var parts = rangeHeader.substring(6).split(",");
        for (String part : parts) {
            var range = part.split("-", 2);
            if (range.length == 2) {
                var startStr = range[0];
                var endStr = range[1];
                var start = !startStr.isEmpty() ? parseLong(startStr) : null;
                var end = !endStr.isEmpty() ? parseLong(endStr) : null;
                ranges.add(new Range(start, end));
            }
        }

        return ranges;
    }

    public long getStart() {
        return start != null ? start : 0L;
    }

    public long getEnd(long fileLength) {
        return end != null ? min(end, fileLength - 1) : fileLength - 1;
    }

}
