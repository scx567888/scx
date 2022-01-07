package cool.scx.util.http;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 模拟表单提交
 * todo 关于大文件上传 待优化(分块读取与发送)
 *
 * @author scx567888
 * @version 1.10.7
 */
public final class FormDataBodyPublisherBuilder {

    private final String lineSeparator = "\r\n";

    private final FormData formData;

    private final String boundary;

    public FormDataBodyPublisherBuilder(FormData formData, String boundary) {
        this.formData = formData;
        this.boundary = boundary;
    }

    /**
     * todo 这里的大文件没有进行分割处理而是直接全部读取到内存中了 后续待优化
     *
     * @param formDataItem a {@link FormData.FormDataItem} object
     * @return an array of {@link byte} objects
     * @throws IOException if any.
     */
    private List<byte[]> toByteArray(FormData.FormDataItem formDataItem) throws IOException {
        if (formDataItem.type == FormData.FormDataItemType.TEXT) {
            var allContent = getHeader(formDataItem) + formDataItem.text + lineSeparator;
            return List.of(allContent.getBytes(StandardCharsets.UTF_8));
        } else if (formDataItem.type == FormData.FormDataItemType.FILE) {
            var headByteArray = getHeader(formDataItem).getBytes(StandardCharsets.UTF_8);
            var fileContentByteArray = Files.readAllBytes(formDataItem.file.toPath());
            var endByteArray = lineSeparator.getBytes(StandardCharsets.UTF_8);
            return List.of(headByteArray, fileContentByteArray, endByteArray);
        } else {
            return List.of();
        }
    }

    /**
     * a
     *
     * @param formDataItem a
     * @return a
     */
    public String getHeader(FormData.FormDataItem formDataItem) {
        var normalHeader = "--" + boundary + lineSeparator + "Content-Disposition: form-data; name=\"" + formDataItem.name + "\"";
        if (formDataItem.type == FormData.FormDataItemType.TEXT) {
            return normalHeader + lineSeparator + "Content-Type: text/plain; charset=UTF-8" + lineSeparator + lineSeparator;
        } else if (formDataItem.type == FormData.FormDataItemType.FILE) {
            return normalHeader + "; filename=\"" + formDataItem.filename + "\"" + lineSeparator + "Content-Type: " + formDataItem.contentType + lineSeparator + lineSeparator;
        } else {
            return "";
        }
    }

    /**
     * <p>build.</p>
     *
     * @return a HttpRequest.BodyPublisher object
     * @throws IOException if any.
     */
    public HttpRequest.BodyPublisher build() throws IOException {
        var iter = new ArrayList<byte[]>();
        for (var formDataItem : formData) {
            iter.addAll(toByteArray(formDataItem));
        }
        //结束标记
        iter.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(iter);
    }

}
