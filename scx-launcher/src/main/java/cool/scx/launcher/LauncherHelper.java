package cool.scx.launcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

final class LauncherHelper {

    public static final String JAR_SUFFIX = ".jar";
    public static final String ZIP_SUFFIX = ".zip";

    public static List<URL> formatLibraries(List<String> libraryPaths) {
        var urlList = new ArrayList<URL>();
        for (var libraryPath : libraryPaths) {
            try {
                Files.walkFileTree(Path.of(libraryPath), new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        if (endsWithIgnoreCase(file.toString(), JAR_SUFFIX) || endsWithIgnoreCase(file.toString(), ZIP_SUFFIX)) {
                            try {
                                urlList.add(file.toUri().toURL());
                            } catch (MalformedURLException e) {
                                //忽略错误
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException exception) {
                //忽略错误
            }
        }
        return urlList;
    }

    public static List<URL> formatClasspath(List<String> classpathPaths) {
        var urlList = new ArrayList<URL>();
        for (var libraryPath : classpathPaths) {
            try {
                var path = Path.of(libraryPath);
                if (Files.isDirectory(path)) {
                    urlList.add(path.toUri().toURL());
                }
            } catch (IOException exception) {
                //忽略错误
            }
        }
        return urlList;
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return str.length() >= suffix.length() && str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length());
    }

}
