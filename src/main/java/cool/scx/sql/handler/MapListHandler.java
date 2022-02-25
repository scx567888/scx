package cool.scx.sql.handler;

import cool.scx.ScxHandlerRE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a
 *
 * @author scx567888
 * @version 1.5.0
 */
public final class MapListHandler implements ScxHandlerRE<ResultSet, List<Map<String, Object>>, SQLException> {

    /**
     * {@inheritDoc}
     * <p>
     * a
     */
    @Override
    public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {
        var list = new ArrayList<Map<String, Object>>();
        var rsm = rs.getMetaData();
        var count = rsm.getColumnCount();
        while (rs.next()) {
            var s = new HashMap<String, Object>();
            for (int i = 1; i <= count; i++) {
                s.put(rsm.getColumnLabel(i), rs.getObject(i));
            }
            list.add(s);
        }
        return list;
    }

}
