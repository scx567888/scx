package cool.scx.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/// ExecUtils todo 整体待重构
///
/// @author scx567888
/// @version 0.0.1
public final class ExecUtils {

    public static String exec(String... cmdArray) throws IOException, InterruptedException {
        var process = Runtime.getRuntime().exec(cmdArray);
        process.waitFor();
        var bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        var stringBuilder = new StringBuilder();
        while (bufferedReader.ready()) {
            stringBuilder.append(bufferedReader.readLine()).append(System.lineSeparator());
        }
        process.destroy();
        return stringBuilder.toString();
    }

}
