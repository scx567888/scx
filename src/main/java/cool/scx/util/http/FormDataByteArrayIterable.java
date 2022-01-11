package cool.scx.util.http;

import cool.scx.exception.ScxExceptionHelper;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static cool.scx.util.http.FormData.lineSeparator;

final class FormDataByteArrayIterable implements Iterator<byte[]> {

    static final byte[] lineSeparatorByteArray = lineSeparator.getBytes(StandardCharsets.UTF_8);
    final ArrayList<FormDataItem> formDataItems;
    final String boundary;
    int nowIndex = 0;
    NowState nowState = NowState.PLEASE_SEND_HEADER;
    FileInputStream nowFileInputStream; //如果当前 formDataItem 是文件则会创建一个 文件流 用于读取文件的字节

    FormDataByteArrayIterable(List<FormDataItem> formDataItemList, String boundary) {
        this.formDataItems = new ArrayList<>(formDataItemList);
        //将最后一位设置为 FINAL_BOUNDARY
        this.formDataItems.add(new FormDataItem(FormDataItemType.FINAL_BOUNDARY));
        this.boundary = boundary;
    }

    @Override
    public boolean hasNext() {
        return nowIndex < formDataItems.size();
    }

    @Override
    public byte[] next() {
        //超出索引返回 null
        if (nowIndex < formDataItems.size()) {
            var nowItem = formDataItems.get(nowIndex);
            //最终块直接返回 并执行 doEnd
            if (nowItem.type == FormDataItemType.FINAL_BOUNDARY) {
                doEnd();
                return ("--" + boundary + "--").getBytes(StandardCharsets.UTF_8);
            }
            //根据当前的状态 判断执行何种操作
            if (nowState == NowState.PLEASE_SEND_HEADER) {//需要发送头
                nowState = NowState.PLEASE_SEND_CONTENT;
                if (nowItem.type == FormDataItemType.FILE) {//如果是文件 这里额外进行一次创建文件流的操作
                    nowFileInputStream = ScxExceptionHelper.wrap(() -> new FileInputStream(nowItem.file));
                }
                return getStart(nowItem);//返回头
            } else if (nowState == NowState.PLEASE_SEND_CONTENT) {//需要发送内容
                if (nowItem.type == FormDataItemType.TEXT) {//文本 直接返回 byte
                    nowState = NowState.PLEASE_SEND_END;
                    return nowItem.text.getBytes(StandardCharsets.UTF_8);
                } else if (nowItem.type == FormDataItemType.FILE_BYTE) {//byte[] 也直接返回 byte
                    nowState = NowState.PLEASE_SEND_END;
                    return nowItem.fileByte;
                } else if (nowItem.type == FormDataItemType.FILE) {//文件则使用 inputStream 发送
                    var cache = new byte[4096]; //缓存使用 4096 大小
                    var i = ScxExceptionHelper.wrap(() -> nowFileInputStream.read(cache));
                    if (i > 0) { //读取到内容 则返回内容
                        return Arrays.copyOfRange(cache, 0, i);
                    } else {// 全部读取完毕 这里直接 处理一下文件流的关闭并 doEnd 即可
                        ScxExceptionHelper.wrap(() -> nowFileInputStream.close());
                        nowFileInputStream = null;
                        return doEnd();
                    }
                }
            } else if (nowState == NowState.PLEASE_SEND_END) {//需要发送结束
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
        nowState = NowState.PLEASE_SEND_HEADER;
        nowIndex = nowIndex + 1;
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
        var start = "--" + boundary + lineSeparator + finalHeader + lineSeparator + lineSeparator;
        return start.getBytes(StandardCharsets.UTF_8);
    }

}