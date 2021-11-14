package cool.scx.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 虚拟文件
 *
 * @author scx567888
 * @version 1.7.3
 */
public final class VirtualFile implements IVirtualFile {

    /**
     * 物理文件
     */
    private final File physicalFile;

    /**
     * 字节数组
     */
    private final byte[] bytes;

    /**
     * 文件名
     */
    private final String fileName;

    /**
     * 虚拟文件类型
     * 0 为源自文件 ,1 为源自字节数组
     */
    private final int type;

    /**
     * <p>Constructor for VirtualFile.</p>
     *
     * @param physicalFile a
     * @param bytes        a
     * @param fileName     a
     * @param type         a
     */
    private VirtualFile(File physicalFile, byte[] bytes, String fileName, int type) {
        this.physicalFile = physicalFile;
        this.bytes = bytes;
        this.fileName = fileName;
        this.type = type;
    }

    /**
     * 通过 文件(File) 创建
     *
     * @param physicalFile a
     * @return a
     */
    public static VirtualFile of(File physicalFile) {
        return new VirtualFile(physicalFile, null, physicalFile.getName(), 0);
    }

    /**
     * 通过 文件() 创建
     *
     * @param name         a
     * @param physicalFile a
     * @return a
     */
    public static VirtualFile of(String name, File physicalFile) {
        return new VirtualFile(physicalFile, null, name, 0);
    }

    /**
     * 通过 byte 创建
     *
     * @param name  a
     * @param bytes a
     * @return a
     */
    public static VirtualFile of(String name, byte[] bytes) {
        return new VirtualFile(null, bytes, name, 1);
    }

    /**
     * 获取虚拟文件的字节
     *
     * @return a
     * @throws java.io.IOException a
     */
    public byte[] getBytes() throws IOException {
        if (type == 0) {
            try (var inputStream = new FileInputStream(this.physicalFile)) {
                return inputStream.readAllBytes();
            }
        } else {
            return bytes;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDirectory() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String path() {
        return fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IVirtualFile findChildren(String name) {
        return null;
    }

}
