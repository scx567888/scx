package cool.scx.logging.spi.jdk;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * ScxJDKLoggerHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
final class ScxJDKLoggerHelper {

    public static String formatMessage(String format, Object... parameters) {
        if (parameters == null || parameters.length == 0) {
            return format;
        }
        try {
            return MessageFormat.format(format, parameters);
        } catch (Exception ex) {
            return format;
        }
    }

    public static String getResourceString(ResourceBundle bundle, String key) {
        if (bundle == null || key == null) {
            return key;
        }
        try {
            return bundle.getString(key);
        } catch (MissingResourceException x) {
            return key;
        }
    }

    public static Throwable getThrowableCandidate(Object[] argArray) {
        return argArray != null && argArray.length != 0 && argArray[argArray.length - 1] instanceof Throwable e ? e : null;
    }

}
