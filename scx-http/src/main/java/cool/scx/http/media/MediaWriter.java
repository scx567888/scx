package cool.scx.http.media;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;

import java.io.OutputStream;

/**
 * 写入器 可用于 ServerResponse 和 ClientRequest
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface MediaWriter {

    /**
     * 写入内容之前 在这里可以设置 header 头
     *
     * @param headersWritable 响应头
     * @param headers         请求头
     */
    void beforeWrite(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers);

    /**
     * 写入内容
     *
     * @param outputStream 输入流
     */
    void write(OutputStream outputStream);

}
