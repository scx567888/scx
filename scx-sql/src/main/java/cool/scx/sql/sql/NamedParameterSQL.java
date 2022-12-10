package cool.scx.sql.sql;


import cool.scx.sql.SQL;
import cool.scx.tuple.Tuple2;
import cool.scx.tuple.Tuples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 具名参数 cool.scx.sql
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class NamedParameterSQL implements SQL {

    /**
     * 具名参数匹配的正则表达式
     */
    private static final Pattern NAMED_PARAMETER_PATTERN = Pattern.compile(":([\\w.-]+)");

    /**
     * 具名参数名称索引
     */
    private final String[] namedParameterNameIndex;

    /**
     * a
     */
    private final PlaceholderSQL placeholderSQL;

    /**
     * a
     *
     * @param namedParameterSQL a
     * @param params            a
     */
    public NamedParameterSQL(String namedParameterSQL, Map<String, Object> params) {
        var t = initNamedParameterNameIndex(namedParameterSQL);
        var normalSQL = t.value0();
        this.namedParameterNameIndex = t.value1();
        this.placeholderSQL = new PlaceholderSQL(normalSQL, mapToArray(params));
    }

    /**
     * a
     *
     * @param namedParameterSQL a
     * @param batchParams       a
     */
    public NamedParameterSQL(String namedParameterSQL, List<Map<String, Object>> batchParams) {
        var t = initNamedParameterNameIndex(namedParameterSQL);
        var normalSQL = t.value0();
        this.namedParameterNameIndex = t.value1();
        this.placeholderSQL = new PlaceholderSQL(normalSQL, batchParams.stream().map(this::mapToArray).toList());
    }

    /**
     * 初始化 namedParameterSQL;
     *
     * @param namedParameterSQL a
     * @return a
     */
    private static Tuple2<String, String[]> initNamedParameterNameIndex(String namedParameterSQL) {
        var matcher = NAMED_PARAMETER_PATTERN.matcher(namedParameterSQL);
        var normalSQL = new StringBuilder();
        var tempNameIndexList = new ArrayList<String>();
        while (matcher.find()) {
            matcher.appendReplacement(normalSQL, "?");
            tempNameIndexList.add(matcher.group(1));
        }
        matcher.appendTail(normalSQL);
        return Tuples.of(normalSQL.toString(), tempNameIndexList.toArray(String[]::new));
    }

    @Override
    public String sql() {
        return placeholderSQL.sql();
    }

    @Override
    public PreparedStatement getPreparedStatement(Connection con) throws SQLException {
        return placeholderSQL.getPreparedStatement(con);
    }

    @Override
    public Object[] params() {
        return placeholderSQL.params();
    }

    /**
     * 根据 namedParameterNameIndex 将 map 转为数组
     *
     * @param objectMap a
     * @return a
     */
    private Object[] mapToArray(Map<String, Object> objectMap) {
        var objectArray = new Object[namedParameterNameIndex.length];
        for (int i = 0; i < namedParameterNameIndex.length; i = i + 1) {
            objectArray[i] = objectMap.get(namedParameterNameIndex[i]);
        }
        return objectArray;
    }

}
