package cool.scx.sql;

import cool.scx.tuple.Tuple2;
import cool.scx.tuple.Tuples;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 具名参数 sql
 *
 * @author scx567888
 * @version 1.5.0
 */
public final class NamedParameterSQL extends AbstractPlaceholderSQL<Map<String, Object>> {

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
     *
     * @param isBatch                 a
     * @param normalSQL               a
     * @param params                  a
     * @param batchParams             a
     * @param namedParameterNameIndex a
     */
    private NamedParameterSQL(boolean isBatch, String normalSQL, Map<String, Object> params, List<Map<String, Object>> batchParams, String[] namedParameterNameIndex) {
        super(isBatch, normalSQL, params, batchParams);
        this.namedParameterNameIndex = namedParameterNameIndex;
    }

    /**
     * a
     *
     * @param namedParameterSQL a
     * @param params            a
     * @return a
     */
    public static NamedParameterSQL of(String namedParameterSQL, Map<String, Object> params) {
        var t = initNamedParameterNameIndex(namedParameterSQL);
        return new NamedParameterSQL(false, t.value0(), params, null, t.value1());
    }

    /**
     * a
     *
     * @param namedParameterSQL a
     * @param batchParams       a
     * @return a
     */
    public static NamedParameterSQL ofBatch(String namedParameterSQL, List<Map<String, Object>> batchParams) {
        if (batchParams == null) {
            batchParams = new ArrayList<>();
        }
        var t = initNamedParameterNameIndex(namedParameterSQL);
        return new NamedParameterSQL(true, t.value0(), null, batchParams, t.value1());
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

    /**
     * 根据 单条参数获取
     *
     * @param con c
     * @return c
     * @throws SQLException c
     */
    @Override
    public PreparedStatement getPreparedStatementFromSingle(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        //单条数据
        if (params != null) {
            fillPreparedStatement(preparedStatement, mapToArray(params));
        }
        return preparedStatement;
    }

    @Override
    public Object[] objectArrayParams() {
        return mapToArray(params);
    }

    /**
     * 根据批量参数获取
     *
     * @param con c
     * @return c
     * @throws SQLException c
     */
    @Override
    public PreparedStatement getPreparedStatementFromBatch(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        //根据数据量, 判断是否使用 批量插入
        for (var paramMap : batchParams) {
            if (paramMap != null) {
                fillPreparedStatement(preparedStatement, mapToArray(paramMap));
                preparedStatement.addBatch();
            }
        }
        return preparedStatement;
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
