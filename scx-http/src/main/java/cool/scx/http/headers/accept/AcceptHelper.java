package cool.scx.http.headers.accept;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/// AcceptHelper
///
/// @author scx567888
/// @version 0.0.1
public class AcceptHelper {

    public static AcceptWritable decodeAccepts(String s) throws IllegalMediaRangeException {
        var list = new ArrayList<MediaRange>();
        var mediaTypes = s.split(",");
        for (var mediaType : mediaTypes) {
            list.add(MediaRange.of(mediaType));
        }
        var sorted = sortMediaRanges(list);
        return new AcceptImpl(sorted);
    }


    // 按 q 值降序 > 类型特异性 > 子类型特异性 排序
    public static List<MediaRange> sortMediaRanges(List<MediaRange> ranges) {
        return ranges.stream()
                .sorted((a, b) -> {
                    // 1. 按 q 值降序
                    int qCompare = Double.compare(b.q(), a.q());
                    if (qCompare != 0) {
                        return qCompare;
                    }

                    // 2. 类型非通配符优先
                    int typeCompare = Boolean.compare(
                            !a.type().equals("*"),
                            !b.type().equals("*")
                    );
                    if (typeCompare != 0) {
                        return -typeCompare;
                    }

                    // 3. 子类型非通配符优先
                    int subtypeCompare = Boolean.compare(
                            !a.subtype().equals("*"),
                            !b.subtype().equals("*")
                    );
                    return -subtypeCompare;
                })
                .collect(Collectors.toList());
    }

}
