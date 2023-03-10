package cool.scx.launcher;

import java.util.List;

public class LauncherTest {

    public static void main(String[] args) throws Exception {
        new Launcher("xxxxx")
                .setLibraries(List.of(""))
                .setClasspath(List.of(""))
                .run();
    }

}
