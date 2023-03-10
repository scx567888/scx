package cool.scx.launcher;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.launcher.LauncherHelper.formatClasspath;
import static cool.scx.launcher.LauncherHelper.formatLibraries;

public final class Launcher {

    private final String mainClass;
    private List<String> classpath = new ArrayList<>();
    private List<String> libraries = new ArrayList<>();
    private String[] args;

    public Launcher(String mainClass) {
        this.mainClass = mainClass;
    }

    public List<String> getClasspath() {
        return classpath;
    }

    public Launcher setClasspath(List<String> classpath) {
        this.classpath = classpath;
        return this;
    }

    public List<String> getLibraries() {
        return libraries;
    }

    public Launcher setLibraries(List<String> libraries) {
        this.libraries = libraries;
        return this;
    }

    public String getMainClass() {
        return mainClass;
    }

    public String[] getArgs() {
        return args;
    }

    public Launcher setArgs(String[] args) {
        this.args = args;
        return this;
    }

    public void run() throws Exception {
        var u1 = formatClasspath(this.getClasspath());
        u1.addAll(formatLibraries(this.getLibraries()));
        var urls = u1.toArray(URL[]::new);

        var cl = new URLClassLoader(urls);
        Thread.currentThread().setContextClassLoader(cl);

        var mainClass = cl.loadClass(this.getMainClass());
        var method = mainClass.getMethod("main", String[].class);
        method.invoke(null, (Object) this.getArgs());
    }

}
