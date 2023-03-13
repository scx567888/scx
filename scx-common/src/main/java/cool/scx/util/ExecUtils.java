package cool.scx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>ExecUtils class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ExecUtils {

    /**
     * <p>exec.</p>
     *
     * @param cmdArray a {@link String} object
     * @return a {@link String} object
     * @throws IOException          if any.
     * @throws InterruptedException if any.
     */
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
