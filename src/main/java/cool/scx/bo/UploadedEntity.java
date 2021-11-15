package cool.scx.bo;

import io.vertx.core.buffer.Buffer;

/**
 * 文件上传后台接受容器类
 * <br>
 * 可以配合 {@link cool.scx.annotation.FromUpload} 注解使用 以实现自定义名称或限制是否必填
 *
 * @author scx567888
 * @version 1.9.1
 */
public record UploadedEntity(String name, String fileName, long size, Buffer buffer, String contentType,
                             String charset) {

}
