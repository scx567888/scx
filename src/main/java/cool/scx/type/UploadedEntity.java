package cool.scx.type;

import cool.scx.ScxContext;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.FileUpload;

/**
 * 文件上传后台接受容器类
 * <br>
 * 可以配合 {@link cool.scx.annotation.FromUpload} 注解使用 以实现自定义名称或限制是否必填
 *
 * @author scx567888
 * @version 1.9.1
 */
public final class UploadedEntity {

    private final FileUpload vertxFileUpload;

    /**
     * a
     *
     * @param fileUpload a
     */
    public UploadedEntity(FileUpload fileUpload) {
        this.vertxFileUpload = fileUpload;
    }

    /**
     * a
     *
     * @return a
     */
    public String name() {
        return vertxFileUpload.name();
    }

    /**
     * a
     *
     * @return a
     */
    public String fileName() {
        return vertxFileUpload.fileName();
    }

    /**
     * a
     *
     * @return a
     */
    public long size() {
        return vertxFileUpload.size();
    }

    /**
     * a
     *
     * @return a
     */
    public Buffer buffer() {
        return ScxContext.vertx().fileSystem().readFileBlocking(vertxFileUpload.uploadedFileName());
    }

    /**
     * a
     *
     * @return a
     */
    public String contentType() {
        return vertxFileUpload.contentType();
    }

    /**
     * a
     *
     * @return a
     */
    public String charset() {
        return vertxFileUpload.charSet();
    }

    /**
     * a
     *
     * @return a
     */
    public FileUpload vertxFileUpload() {
        return vertxFileUpload;
    }

}
