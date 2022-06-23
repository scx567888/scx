package cool.scx.util.http;

import cool.scx.util.RandomUtils;
import cool.scx.util.exception.ScxExceptionHelper;
import io.vertx.core.http.impl.MimeMapping;

import java.io.File;
import java.io.FileInputStream;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FormData implements Body {

    /**
     * a
     */
    private static final String FORM_BOUNDARY_PREFIX = "----ScxHttpClientHelperFormBoundary";

    /**
     * Constant <code>lineSeparator="\r\n"</code>
     */
    private static final String lineSeparator = "\r\n";

    /**
     * Constant <code>lineSeparatorByteArray</code>
     */
    private static final byte[] lineSeparatorByteArray = lineSeparator.getBytes(StandardCharsets.UTF_8);

    private final List<FormDataItem> formDataItemList = new ArrayList<>();

    /**
     * a
     *
     * @param name a
     * @param text a
     * @return a
     */
    public FormData add(String name, Object text) {
        this.formDataItemList.add(new FormDataItem(name, text.toString()));
        return this;
    }

    /**
     * a
     *
     * @param name a
     * @param file a
     * @return a
     */
    public FormData addFile(String name, File file) {
        this.formDataItemList.add(new FormDataItem(name, file));
        return this;
    }

    /**
     * a
     *
     * @param name        a
     * @param fileByte    a
     * @param filename    a
     * @param contentType a
     * @return a
     */
    public FormData addFile(String name, byte[] fileByte, String filename, String contentType) {
        this.formDataItemList.add(new FormDataItem(name, fileByte, filename, contentType));
        return this;
    }

    /**
     * a
     *
     * @param name     a
     * @param fileByte a
     * @param filename a
     * @return a
     */
    public FormData addFile(String name, byte[] fileByte, String filename) {
        this.formDataItemList.add(new FormDataItem(name, fileByte, filename));
        return this;
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public FormData remove(String name) {
        this.formDataItemList.removeIf(formDataItem -> name.equals(formDataItem.name));
        return this;
    }

    /**
     * a
     *
     * @param builder a a
     * @return a
     */
    @Override
    public HttpRequest.BodyPublisher getBodyPublisher(HttpRequest.Builder builder) {
        final String boundary = FORM_BOUNDARY_PREFIX + RandomUtils.getRandomString(8, true);
        builder.setHeader("content-type", "multipart/form-data; boundary=" + boundary);
        return HttpRequest.BodyPublishers.ofByteArrays(() -> new FormDataByteArrayIterable(formDataItemList, boundary));
    }

    enum FormDataItemType {
        CONTENT, FILE, FINAL_BOUNDARY
    }

    enum NowState {
        PLEASE_SEND_HEADER, PLEASE_SEND_CONTENT, PLEASE_SEND_END
    }

    static final class FormDataByteArrayIterable implements Iterator<byte[]> {

        final FormDataItem[] formDataItems;
        final String boundary;
        int nowIndex = 0;
        NowState nowState = NowState.PLEASE_SEND_HEADER;
        FileInputStream nowFileInputStream; //如果当前 formDataItem 是文件则会创建一个 文件流 用于读取文件的字节

        FormDataByteArrayIterable(List<FormDataItem> formDataItemList, String boundary) {
            this.formDataItems = new FormDataItem[formDataItemList.size() + 1];
            System.arraycopy(formDataItemList.toArray(FormDataItem[]::new), 0, this.formDataItems, 0, formDataItemList.size());
            //将最后一位设置为 FINAL_BOUNDARY
            this.formDataItems[this.formDataItems.length - 1] = new FormDataItem(FormDataItemType.FINAL_BOUNDARY);
            this.boundary = boundary;
        }

        @Override
        public boolean hasNext() {
            return this.nowIndex < this.formDataItems.length;
        }

        @Override
        public byte[] next() {
            if (hasNext()) {
                var nowItem = this.formDataItems[nowIndex];
                //最终块直接返回 并执行 doEnd
                if (nowItem.type == FormDataItemType.FINAL_BOUNDARY) {
                    doEnd();
                    return ("--" + this.boundary + "--").getBytes(StandardCharsets.UTF_8);
                }
                //根据当前的状态 判断执行何种操作
                if (this.nowState == NowState.PLEASE_SEND_HEADER) {//需要发送头
                    this.nowState = NowState.PLEASE_SEND_CONTENT;
                    if (nowItem.type == FormDataItemType.FILE) {//如果是文件 这里额外进行一次创建文件流的操作
                        this.nowFileInputStream = ScxExceptionHelper.wrap(() -> new FileInputStream(nowItem.file));
                    }
                    return getStart(nowItem);//返回头
                } else if (this.nowState == NowState.PLEASE_SEND_CONTENT) {// 需要发送内容
                    if (nowItem.type == FormDataItemType.CONTENT) {// CONTENT 类型的 可以一次性返回
                        this.nowState = NowState.PLEASE_SEND_END;
                        return nowItem.content;
                    } else if (nowItem.type == FormDataItemType.FILE) {//文件则使用 inputStream 分多次返回
                        var cache = new byte[4096]; //缓存使用 4KB 大小
                        var i = ScxExceptionHelper.wrap(() -> this.nowFileInputStream.read(cache));
                        if (i > 0) { //读取到内容 则返回内容
                            return Arrays.copyOfRange(cache, 0, i);
                        } else {// 全部读取完毕 这里直接 处理一下文件流的关闭并 doEnd 即可
                            ScxExceptionHelper.wrap(() -> this.nowFileInputStream.close());
                            this.nowFileInputStream = null;
                            return doEnd();
                        }
                    }
                } else if (this.nowState == NowState.PLEASE_SEND_END) {//需要发送结束
                    return doEnd();
                }
            }
            return null;
        }

        /**
         * 将 状态设置为需要 获取头 并向后移动索引
         *
         * @return 换行
         */
        byte[] doEnd() {
            this.nowState = NowState.PLEASE_SEND_HEADER;
            this.nowIndex = this.nowIndex + 1;
            return lineSeparatorByteArray;
        }

        /**
         * 获取头
         *
         * @param f f
         * @return a
         */
        byte[] getStart(FormDataItem f) {
            var headerStr = f.filename == null ?
                    "Content-Disposition: form-data; name=\"" + f.name + "\"" :
                    "Content-Disposition: form-data; name=\"" + f.name + "\"; filename=\"" + f.filename + "\"";
            var finalHeader = f.contentType == null ?
                    headerStr :
                    headerStr + lineSeparator + "Content-Type: " + f.contentType;
            var start = "--" + this.boundary + lineSeparator + finalHeader + lineSeparator + lineSeparator;
            return start.getBytes(StandardCharsets.UTF_8);
        }

    }

    static final class FormDataItem {
        final FormDataItemType type;
        String name;
        File file;
        byte[] content;
        String filename;
        String contentType;

        FormDataItem(FormDataItemType type) {
            this.type = type;
        }

        FormDataItem(String name, Object text) {
            this.type = FormDataItemType.CONTENT;
            this.name = name;
            this.content = text.toString().getBytes(StandardCharsets.UTF_8);
        }

        FormDataItem(String name, byte[] content, String filename, String contentType) {
            this.type = FormDataItemType.CONTENT;
            this.name = name;
            this.content = content;
            this.filename = filename;
            this.contentType = contentType;
        }

        FormDataItem(String name, byte[] content, String filename) {
            this.type = FormDataItemType.CONTENT;
            this.name = name;
            this.content = content;
            this.filename = filename;
            this.contentType = MimeMapping.getMimeTypeForFilename(this.filename);
        }

        FormDataItem(String name, File file) {
            this.type = FormDataItemType.FILE;
            this.name = name;
            this.file = file;
            this.filename = file.getName();
            this.contentType = MimeMapping.getMimeTypeForFilename(this.filename);
        }

    }

}
