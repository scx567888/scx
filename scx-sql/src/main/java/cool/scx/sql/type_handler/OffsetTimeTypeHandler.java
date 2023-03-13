package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetTime;

//todo test
public class OffsetTimeTypeHandler implements TypeHandler<OffsetTime> {

    @Override
    public OffsetTime getObject(ResultSet rs, int index) throws SQLException {
        return rs.getObject(index, OffsetTime.class);
    }

}
