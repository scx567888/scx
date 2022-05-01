package cool.scx.util.file;

class FileUtilsOptionInfo {

    public final boolean excludeRoot;

    public FileUtilsOptionInfo(FileUtilsOption... options) {
        var _excludeRoot = false;
        for (var option : options) {
            switch (option) {
                case EXCLUDE_ROOT -> _excludeRoot = true;
            }
        }
        this.excludeRoot = _excludeRoot;
    }

    public boolean excludeRoot() {
        return excludeRoot;
    }
}