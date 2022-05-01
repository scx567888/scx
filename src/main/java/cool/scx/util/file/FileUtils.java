package cool.scx.util.file;

import cool.scx.util.HexUtils;
import cool.scx.util.StringUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 文件 操作类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class FileUtils {

    /**
     * 文件大小格式化 正则表达式
     */
    public final static Pattern DISPLAY_SIZE_PATTERN = Pattern.compile("^([+\\-]?\\d+)([a-zA-Z]{0,2})$");

    /**
     * Constant <code>WINDOWS_FILE_SEPARATOR="\\&quot;"</code>
     */
    private static final String WINDOWS_FILE_SEPARATOR = "\\";

    /**
     * Constant <code>UNIX_FILE_SEPARATOR="/"</code>
     */
    private static final String UNIX_FILE_SEPARATOR = "/";

    /**
     * Constant <code>FILE_EXTENSION="."</code>
     */
    private static final String FILE_EXTENSION = ".";

    /**
     * 文件大小格式化 映射表 方便计算使用
     */
    private final static HashMap<String, Long> DISPLAY_SIZE_MAP = initDisplaySizeMap();

    /**
     * <p>getDisplaySizeMap.</p>
     *
     * @return a {@link java.util.HashMap} object
     */
    private static HashMap<String, Long> initDisplaySizeMap() {
        var tempMap = new HashMap<String, Long>();
        tempMap.put("B", 1L);
        tempMap.put("KB", 1024L);
        tempMap.put("MB", 1024 * 1024L);
        tempMap.put("GB", 1024 * 1024 * 1024L);
        tempMap.put("TB", 1024 * 1024 * 1024 * 1024L);
        return tempMap;
    }

    /**
     * 将 long 类型的文件大小 格式化(转换为人类可以看懂的形式)
     * 如 1024 转换为 1KB
     *
     * @param size a long.
     * @return a {@link java.lang.String} object.
     */
    public static String longToDisplaySize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 将 格式化后的大小转换为 long
     * 如将 1KB 转换为 1024
     *
     * @param str 待转换的值 如 5MB 13.6GB
     * @return a long.
     */
    public static long displaySizeToLong(String str) {
        var matcher = DISPLAY_SIZE_PATTERN.matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(str + " : 无法转换为 long");
        }
        var group = matcher.group(2);
        long amount = Long.parseLong(matcher.group(1));
        var s = StringUtils.isNotBlank(group) ? DISPLAY_SIZE_MAP.get(group) : DISPLAY_SIZE_MAP.get("B");
        return Math.multiplyExact(amount, s);
    }

    /**
     * 删除文件或文件夹(会删除文件树中所有内容)
     *
     * @param start   a
     * @param options a
     * @throws IOException a
     */
    public static void delete(Path start, FileUtilsOption... options) throws IOException {
        var info = new FileUtilsOptionInfo(options);
        Files.walkFileTree(start, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                var dontNeedDelete = info.excludeRoot() && Files.isSameFile(start, dir);
                if (!dontNeedDelete) {
                    Files.delete(dir);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * a
     *
     * @param source  a
     * @param target  a
     * @param options a
     * @throws IOException a
     */
    public static void move(Path source, Path target, CopyOption... options) throws IOException {
        var defaultOptions = new CopyOption[]{StandardCopyOption.REPLACE_EXISTING};
        Files.createDirectories(target.getParent());
        Files.move(source, target, options.length == 0 ? defaultOptions : options);
    }

    /**
     * @param path    a
     * @param bytes   a
     * @param options a
     * @throws IOException a
     */
    public static void write(Path path, byte[] bytes, OpenOption... options) throws IOException {
        var defaultOptions = new OpenOption[]{StandardOpenOption.APPEND, StandardOpenOption.CREATE, StandardOpenOption.SYNC, StandardOpenOption.WRITE};
        Files.createDirectories(path.getParent());
        Files.write(path, bytes, options.length == 0 ? defaultOptions : options);
    }

    /**
     * 获取拓展名 (不包括 . )
     *
     * @param fileName a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String getExt(String fileName) {
        if (fileName == null) {
            return null;
        }
        String extension = "";
        int indexOfLastExtension = fileName.lastIndexOf(FILE_EXTENSION);

        // check last file separator, windows and unix
        int lastSeparatorPosWindows = fileName.lastIndexOf(WINDOWS_FILE_SEPARATOR);
        int lastSeparatorPosUnix = fileName.lastIndexOf(UNIX_FILE_SEPARATOR);

        // takes the greater of the two values, which mean last file separator
        int indexOfLastSeparator = Math.max(lastSeparatorPosWindows, lastSeparatorPosUnix);

        // make sure the file extension appear after the last file separator
        if (indexOfLastExtension > indexOfLastSeparator) {
            extension = fileName.substring(indexOfLastExtension + 1);
        }

        return extension;
    }

    /**
     * 获取 文件 head
     *
     * @param filePath f
     * @param length   a int
     * @return r
     * @throws java.io.IOException if any.
     */
    public static String getHead(String filePath, int length) throws IOException {
        try (var accessFile = new RandomAccessFile(filePath, "r")) {
            var headByteArray = new byte[length];
            accessFile.read(headByteArray);
            return HexUtils.toHex(headByteArray);
        }
    }

}
