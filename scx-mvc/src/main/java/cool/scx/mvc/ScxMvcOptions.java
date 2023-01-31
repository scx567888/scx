package cool.scx.mvc;

import cool.scx.util.FileUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ScxMvcOptions {

    private final List<Class<?>> classList = new ArrayList<>();
    private boolean useDevelopmentErrorPage;
    private String allowedOrigin;
    private Path uploadsDirectory;
    private long bodyLimit;

    public ScxMvcOptions() {
        // 默认 http 请求 body 限制大小
        this.bodyLimit = FileUtils.displaySizeToLong("16384KB");
        this.allowedOrigin = "*";
    }

    public boolean useDevelopmentErrorPage() {
        return this.useDevelopmentErrorPage;
    }

    public String allowedOrigin() {
        return this.allowedOrigin;
    }

    public Path uploadsDirectory() {
        return this.uploadsDirectory;
    }

    public long bodyLimit() {
        return this.bodyLimit;
    }

    public ScxMvcOptions useDevelopmentErrorPage(boolean useDevelopmentErrorPage) {
        this.useDevelopmentErrorPage = useDevelopmentErrorPage;
        return this;
    }

    public ScxMvcOptions allowedOrigin(String allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
        return this;
    }

    public ScxMvcOptions uploadsDirectory(Path uploadsDirectory) {
        this.uploadsDirectory = uploadsDirectory;
        return this;
    }

    public ScxMvcOptions bodyLimit(long bodyLimit) {
        this.bodyLimit = bodyLimit;
        return this;
    }

    public List<Class<?>> classList() {
        return classList;
    }

    //todo 或许应该从 options 中移除
    public ScxMvcOptions addClass(Class<?>... classes) {
        classList.addAll(List.of(classes));
        return this;
    }

}
