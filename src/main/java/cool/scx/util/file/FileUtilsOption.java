package cool.scx.util.file;

/**
 * a
 */
public enum FileUtilsOption {

    /**
     * 实现清空文件夹的效果
     * 排除根目录 (删除文件为 "文件" 时无效, "目录" 时有效)
     * 比如 未使用此选项调用 delete("/user/test") 文件夹 则 test 文件夹会被删除
     * 若使用此选项则 会清空 test 下所有文件 test 目录则会保留
     */
    EXCLUDE_ROOT;

    static class Info {

        public boolean excludeRoot;

        public Info(FileUtilsOption... options) {
            for (var option : options) {
                switch (option) {
                    case EXCLUDE_ROOT -> this.excludeRoot = true;
                }
            }
        }

        public boolean excludeRoot() {
            return excludeRoot;
        }

    }

}