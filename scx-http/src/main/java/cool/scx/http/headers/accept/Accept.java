package cool.scx.http.headers.accept;

import cool.scx.http.media_type.ScxMediaType;

/// Accept
///
/// @author scx567888
/// @version 0.0.1
public interface Accept extends Iterable<MediaRange> {

    static AcceptWritable of(String acceptsStr) {
        return AcceptHelper.decodeAccepts(acceptsStr);
    }

    long size();

    /// 判断提供的 媒体类型是否支持 内部采用 模糊匹配
    boolean isAcceptable(ScxMediaType mediaType);

    /// 判断提供的 媒体类型是否包含 内部采用 精确匹配
    boolean contains(ScxMediaType mediaTypes);

    /// 根据提供的媒体类型列表 找到最优支持
    <T extends ScxMediaType> T negotiate(T... mediaTypes);

}
