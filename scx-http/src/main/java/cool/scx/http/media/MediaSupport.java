package cool.scx.http.media;

import cool.scx.http.ScxHttpServerRequestHeaders;

import java.io.InputStream;

/**
 * 类型支持
 * todo 暂时只实现读取 后续需要实现写入 并用于 response 的 send 方法
 *
 * @param <T>
 */
public interface MediaSupport<T> {

    T read(InputStream inputStream, ScxHttpServerRequestHeaders headers);

    // todo 待完成
//    void write(OutputStream inputStream, ScxHttpServerRequestHeaders headers);

}
