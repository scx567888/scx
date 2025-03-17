package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/// AcceptsImpl
///
/// @author scx567888
/// @version 0.0.1
public class AcceptImpl implements AcceptWritable {

    /// 在进行匹配的时候 请将此 List 进行排序
    private final List<MediaRange> mediaRanges;

    public AcceptImpl() {
        this.mediaRanges = new ArrayList<>();
    }

    public AcceptImpl(List<MediaRange> sorted) {
        this.mediaRanges = sorted;
    }

    @Override
    public long size() {
        return mediaRanges.size();
    }

    @Override
    public boolean isAcceptable(ScxMediaType mediaType) {
        // 遍历所有 MediaRange，判断是否有任意一个匹配该 MediaType
        for (var range : mediaRanges) {
            if (range.matches(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    @Override
    public final <T extends ScxMediaType> T negotiate(T... serverSupportedTypes) {
        // 遍历已排序的 MediaRange，找到第一个匹配的服务端支持的类型
        for (MediaRange range : mediaRanges) {
            for (var supportedType : serverSupportedTypes) {
                if (range.matches(supportedType)) {
                    return supportedType;
                }
            }
        }
        return null;
    }

    @Override
    public Iterator<MediaRange> iterator() {
        return mediaRanges.iterator();
    }

    @Override
    public AcceptWritable add(MediaRange mediaRange) {
        this.mediaRanges.add(mediaRange);
        return this;
    }

    @Override
    public AcceptWritable remove(MediaRange mediaRange) {
        this.mediaRanges.remove(mediaRange);
        return this;
    }

}
