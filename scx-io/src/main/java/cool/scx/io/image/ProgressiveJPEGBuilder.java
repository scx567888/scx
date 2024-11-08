package cool.scx.io.image;

import cool.scx.io.InputSource;
import cool.scx.io.input_source.ByteArrayInputSource;
import cool.scx.io.input_source.FileInputSource;
import cool.scx.io.input_source.InputStreamInputSource;
import cool.scx.io.input_source.LazyInputStreamInputSource;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;

/**
 * 将图片转换为 渐进式 JPEG
 */
public final class ProgressiveJPEGBuilder extends LazyInputStreamInputSource {

    private final BufferedImage sourceImage;

    public ProgressiveJPEGBuilder(InputSource source) throws IOException {
        //源文件
        this.sourceImage = ImageIO.read(source.toInputStream());
    }

    public ProgressiveJPEGBuilder(Path path) throws IOException {
        this(new FileInputSource(path));
    }

    public ProgressiveJPEGBuilder(byte[] bytes) throws IOException {
        this(new ByteArrayInputSource(bytes));
    }

    public ProgressiveJPEGBuilder(InputStream inputStream) throws IOException {
        this(new InputStreamInputSource(inputStream));
    }

    public static ImageWriter getImageWriter(String formatName) {
        var writerIterator = ImageIO.getImageWritersByFormatName(formatName.trim().toLowerCase());
        var writer = writerIterator.next();
        if (writer == null) {
            throw new IllegalArgumentException("未找到对应格式的 ImageWriter , formatName : " + formatName);
        }
        return writer;
    }

    @Override
    protected InputStream toInputStream0() throws IOException {
        //图片 writer
        var jpegWriter = getImageWriter("jpeg");
        //参数
        var writeParam = jpegWriter.getDefaultWriteParam();
        //使用默认参数以启用渐进式图片
        writeParam.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
        
        var out = new ByteArrayOutputStream();
        //开始转换
        try (var m = new MemoryCacheImageOutputStream(out)) {
            jpegWriter.setOutput(m);
            jpegWriter.write(null, new IIOImage(sourceImage, null, null), writeParam);
            jpegWriter.dispose();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

}
