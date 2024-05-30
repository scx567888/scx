package cool.scx.web;

import java.nio.file.Path;

public class ScxWebOptions {

    private Path templateRoot;
    private boolean useDevelopmentErrorPage;

    public ScxWebOptions() {
        this.useDevelopmentErrorPage = false;
        this.templateRoot = null;
    }

    public Path templateRoot() {
        return this.templateRoot;
    }

    public ScxWebOptions templateRoot(Path templateRoot) {
        this.templateRoot = templateRoot;
        return this;
    }

    public ScxWebOptions useDevelopmentErrorPage(boolean useDevelopmentErrorPage) {
        this.useDevelopmentErrorPage = useDevelopmentErrorPage;
        return this;
    }

    public boolean useDevelopmentErrorPage() {
        return useDevelopmentErrorPage;
    }

}
