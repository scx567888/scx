package cool.scx.mvc;

import java.nio.file.Path;

public class ScxMvcOptions {

    private Path templateRoot;
    private boolean useDevelopmentErrorPage;

    public ScxMvcOptions() {
        this.useDevelopmentErrorPage = false;
        this.templateRoot = null;
    }

    public Path templateRoot() {
        return this.templateRoot;
    }

    public ScxMvcOptions templateRoot(Path templateRoot) {
        this.templateRoot = templateRoot;
        return this;
    }

    public ScxMvcOptions useDevelopmentErrorPage(boolean useDevelopmentErrorPage) {
        this.useDevelopmentErrorPage = useDevelopmentErrorPage;
        return this;
    }

    public boolean useDevelopmentErrorPage() {
        return useDevelopmentErrorPage;
    }

}
