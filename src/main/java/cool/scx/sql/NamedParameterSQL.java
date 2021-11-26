package cool.scx.sql;

import com.mysql.cj.jdbc.ClientPreparedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * a
 *
 * @author scx567888
 * @version 1.5.0
 */
final class NamedParameterSQL extends AbstractPlaceholderSQL {

    /**
     * 具名参数匹配的正则表达式
     */
    private static final Pattern NAMED_PARAMETER_PATTERN = Pattern.compile(":([a-zA-Z0-9_.-]+)");

    /**
     * 具名参数名称索引
     */
    private final List<String> namedParameterNameIndex = new ArrayList<>();

    /**
     * a
     */
    private final Collection<Map<String, Object>> params;

    /**
     * <p>Constructor for NamedParameterSQLConverter.</p>
     *
     * @param namedParameterSQL a {@link java.lang.String} object
     */
    public NamedParameterSQL(String namedParameterSQL, Map<String, Object> param) {
        this(namedParameterSQL, param != null ? List.of(param) : null);
    }

    /**
     * 批量
     *
     * @param namedParameterSQL a
     * @param params            a
     */
    public NamedParameterSQL(String namedParameterSQL, Collection<Map<String, Object>> params) {
        var matcher = NAMED_PARAMETER_PATTERN.matcher(namedParameterSQL);
        var normalSQL = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(normalSQL, "?");
            this.namedParameterNameIndex.add(matcher.group(1));
        }
        matcher.appendTail(normalSQL);
        this.normalSQL = normalSQL.toString();
        this.params = params != null ? params : new ArrayList<>();
    }

    @Override
    public PreparedStatement getPreparedStatement(Connection con) throws SQLException {
        var preparedStatement = con.prepareStatement(normalSQL, Statement.RETURN_GENERATED_KEYS);
        //根据数据量, 判断是否使用 批量插入
        var isBatch = params.size() > 1;
        for (var paramMap : params) {
            if (paramMap != null) {
                fillPreparedStatement(preparedStatement, namedParameterNameIndex.stream().map(paramMap::get).toArray());
                if (isBatch) {
                    preparedStatement.addBatch();
                }
            }
        }
        if (SQLRunner.logger.isDebugEnabled()) {
            var realSQL = preparedStatement.unwrap(ClientPreparedStatement.class).asSql();
            if (isBatch) {
                SQLRunner.logger.debug(realSQL + "... 额外的 " + (params.size() - 1) + " 项");
            } else {
                SQLRunner.logger.debug(realSQL);
            }
        }
        return preparedStatement;
    }

}
