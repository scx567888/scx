package cool.scx.web;

import java.nio.file.Path;

/**
 * ScxWebOptions
 *
 * @author scx567888
 * @version 0.0.1
 */
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
