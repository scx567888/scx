package cool.scx.jdbc.sql;

import java.sql.SQLException;

public class SQLRunnerException extends RuntimeException {

    public SQLRunnerException(String message, SQLException sqlException) {
        super(message, sqlException);
    }

    public SQLRunnerException(SQLException sqlException) {
        super(sqlException);
    }

    @Override
    public SQLException getCause() {
        return (SQLException) super.getCause();
    }

}
