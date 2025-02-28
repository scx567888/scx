package cool.scx.web;

import java.nio.file.Path;

/// ScxWebOptions
///
/// @author scx567888
/// @version 0.0.1
public class ScxWebOptions {

    private Path templateRoot;
    private boolean useDevelopmentErrorPage;
    private boolean cachedMultiPart;

    public ScxWebOptions() {
        this.useDevelopmentErrorPage = false;
        this.templateRoot = null;
        this.cachedMultiPart = false;
    }

    public Path templateRoot() {
        return this.templateRoot;
    }

    public ScxWebOptions templateRoot(Path templateRoot) {
        this.templateRoot = templateRoot;
        return this;
    }

    public boolean useDevelopmentErrorPage() {
        return useDevelopmentErrorPage;
    }

    public ScxWebOptions useDevelopmentErrorPage(boolean useDevelopmentErrorPage) {
        this.useDevelopmentErrorPage = useDevelopmentErrorPage;
        return this;
    }

    public boolean cachedMultiPart() {
        return cachedMultiPart;
    }

    public ScxWebOptions cachedMultiPart(boolean cachedMultiPart) {
        this.cachedMultiPart = cachedMultiPart;
        return this;
    }

}
