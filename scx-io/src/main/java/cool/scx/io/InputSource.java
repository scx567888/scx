package cool.scx.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * 可以将其想象成一个 BytesSupplier ,主要用来规范多种类型的数据来源 如文件 字节数组等
 *
 * @author scx567888
 * @version 2.0.4
 */
public interface InputSource extends AutoCloseable {

    /**
     * 读取为 字节数组
     *
     * @param length 最大长度
     * @return 字节数组
     * @throws IOException E
     */
    byte[] read(int length) throws IOException;

    /**
     * 读取所有字节
     *
     * @return 字节数组
     * @throws IOException E
     */
    byte[] readAll() throws IOException;

    /**
     * 写入到 输出流中
     *
     * @param out out
     * @return 写入长度
     * @throws IOException E
     */
    long transferTo(OutputStream out) throws IOException;

    /**
     * 写入到 文件中
     *
     * @param outputPath 文件路径
     * @param options    文件开启选项
     * @throws IOException E
     */
    void transferTo(Path outputPath, OpenOption... options) throws IOException;

    /**
     * 获取一个输入流 为了兼容一些其他库
     *
     * @return 输入流
     * @throws IOException E
     */
    InputStream toInputStream() throws IOException;

}
