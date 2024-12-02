package cool.scx.jdbc.spy.event_listener;

import cool.scx.jdbc.dialect.Dialect;

import java.lang.System.Logger;
import java.sql.Statement;

import static java.lang.System.Logger.Level.DEBUG;

/**
 * LoggingEventListener
 *
 * @author scx567888
 * @version 0.0.1
 */
public class LoggingEventListener extends EasyEventListener {

    private static final Logger logger = System.getLogger("ScxSpy");
    private final Dialect dialect;

    public LoggingEventListener(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public void onBeforeAnyExecute(Statement statement) {
        if (logger.isLoggable(DEBUG)) {
            logger.log(DEBUG, dialect.getFinalSQL(statement));
        }
    }

}
