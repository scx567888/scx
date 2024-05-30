package cool.scx.common.util;

import java.util.Scanner;

/**
 * 控制台工具类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class ConsoleUtils {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static String readLine() {
        return SCANNER.nextLine();
    }

}
