package cool.scx.common.util.image;

import cool.scx.common.util.io_stream_source.InputStreamSource;
import cool.scx.common.util.io_stream_source.OutputStreamSource;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * 将图片转换为 渐进式 JPEG
 */
public final class ProgressiveJPEGBuilder implements OutputStreamSource {

    private final BufferedImage sourceImage;

    public ProgressiveJPEGBuilder(InputStreamSource source) throws IOException {
        //源文件
        this.sourceImage = ImageIO.read(source.toInputStream());
    }

    public ProgressiveJPEGBuilder(Path path) throws IOException {
        this(InputStreamSource.of(path));
    }

    public ProgressiveJPEGBuilder(byte[] bytes) throws IOException {
        this(InputStreamSource.of(bytes));
    }

    public ProgressiveJPEGBuilder(Supplier<byte[]> bytesSupplier) throws IOException {
        this(InputStreamSource.of(bytesSupplier));
    }

    public ProgressiveJPEGBuilder(InputStream inputStream) throws IOException {
        this(InputStreamSource.of(inputStream));
    }

    /**
     * <p>getImageWriter.</p>
     *
     * @param formatName a {@link java.lang.String} object
     * @return a {@link javax.imageio.ImageWriter} object
     */
    public static ImageWriter getImageWriter(String formatName) {
        var writerIterator = ImageIO.getImageWritersByFormatName(formatName.trim().toLowerCase());
        var writer = writerIterator.next();
        if (writer == null) {
            throw new IllegalArgumentException("未找到对应格式的 ImageWriter , formatName : " + formatName);
        }
        return writer;
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        //图片 writer
        var jpegWriter = getImageWriter("jpeg");
        //参数
        var writeParam = jpegWriter.getDefaultWriteParam();
        //使用默认参数以启用渐进式图片
        writeParam.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
        //开始转换
        try (var m = new MemoryCacheImageOutputStream(out)) {
            jpegWriter.setOutput(m);
            jpegWriter.write(null, new IIOImage(sourceImage, null, null), writeParam);
            jpegWriter.dispose();
        }
    }

}
