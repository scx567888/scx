package cool.scx.sql;

import cool.scx.functional.ScxFunction;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>ResultHandler interface.</p>
 *
 * @author scx567888
 * @version 0.0.7
 */
public interface ResultHandler<T> extends ScxFunction<ResultSet, T, SQLException> {

}
