package cool.scx.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;

import java.util.Arrays;

/**
 * <p>VoBytesWriter class.</p>
 *
 * @author scx567888
 * @version 1.3.5
 */
public record VoBytesWriter(byte[] bytes) {

    /**
     * 节流桶的大小
     * 不宜过大 会造成内存占用过高 , 也不建议过小会造成请求写入过于频繁
     * 和 pauseTime 进行配合可以实现对传输的速度限制
     */
    private static final int bucketSize = 1024 * 1000;

    /**
     * <p>writeBytes.</p>
     *
     * @param response   response
     * @param startIndex 起始索引
     */
    public void writeBytes(HttpServerResponse response, int startIndex) {
        //当前分块的尾部索引
        var endIndex = startIndex + bucketSize;
        //尾部索引 大于等于 字节长度 说明是最后一个区块
        if (endIndex >= bytes.length) {
            response.end(Buffer.buffer(Arrays.copyOfRange(bytes, startIndex, bytes.length)));
        } else {//不是最后一个区块
            response.write(Buffer.buffer(Arrays.copyOfRange(bytes, startIndex, endIndex)), (r) -> {
                if (r.succeeded()) {
                    //将尾部索引作为下一次递归的起始索引
                    writeBytes(response, endIndex);
                }
            });
        }
    }

}
