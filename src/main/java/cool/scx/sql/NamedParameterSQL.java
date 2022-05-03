package cool.scx.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * a
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
     * @param namedParameterSQL a
     * @param params            a
     */
    public NamedParameterSQL(String namedParameterSQL, Map<String, Object> params) {
        super(false);
        this.namedParameterNameIndex = initNamedParameterNameIndex(namedParameterSQL);
        this.params = params;
    }

    /**
     * 批量
     *
     * @param namedParameterSQL a
     * @param batchParams       a
     */
    public NamedParameterSQL(String namedParameterSQL, List<Map<String, Object>> batchParams) {
        super(true);
        this.namedParameterNameIndex = initNamedParameterNameIndex(namedParameterSQL);
        this.batchParams = batchParams != null ? batchParams : new ArrayList<>();
    }

    /**
     * 初始化 namedParameterSQL;
     *
     * @param namedParameterSQL a
     */
    private String[] initNamedParameterNameIndex(String namedParameterSQL) {
        var matcher = NAMED_PARAMETER_PATTERN.matcher(namedParameterSQL);
        var normalSQL = new StringBuilder();
        var tempNameIndexList = new ArrayList<String>();
        while (matcher.find()) {
            matcher.appendReplacement(normalSQL, "?");
            tempNameIndexList.add(matcher.group(1));
        }
        matcher.appendTail(normalSQL);
        this.normalSQL = normalSQL.toString();
        return tempNameIndexList.toArray(String[]::new);
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
        for (int i1 = 0; i1 < namedParameterNameIndex.length; i1++) {
            objectArray[i1] = objectMap.get(namedParameterNameIndex[i1]);
        }
        return objectArray;
    }

}
