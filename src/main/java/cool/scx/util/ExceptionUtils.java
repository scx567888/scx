package cool.scx.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtils {

    public static String getStackTrace(Throwable throwable) {
        var stringWriter = new StringWriter();
        try (var printWriter = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(printWriter);
            return stringWriter.toString();
        }
    }

}
