package cool.scx.util.zip;

/**
 * a
 *
 * @author scx567888
 * @version 2.0.4
 */
public enum ZipOption {

    /**
     * 是否包含根目录
     * 压缩时使用则会直接压缩整个文件夹, 否则相当于压缩文件内文件夹
     * 解压时使用则会在待解压目录创建与压缩包名称相同的文件夹
     * 只针对文件夹生效
     */
    INCLUDE_ROOT,

    /**
     * 是否使用原始文件名 使用时 zipPath 将会变为文件夹
     */
    USE_ORIGINAL_FILE_NAME;

    static class Info {

        boolean includeRoot;
        boolean useOriginalFileName;

        Info(ZipOption... options) {
            for (var option : options) {
                switch (option) {
                    case INCLUDE_ROOT -> this.includeRoot = true;
                    case USE_ORIGINAL_FILE_NAME -> this.useOriginalFileName = true;
                }
            }
        }

        boolean includeRoot() {
            return includeRoot;
        }

        boolean useOriginalFileName() {
            return useOriginalFileName;
        }

    }

}
