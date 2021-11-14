package cool.scx.sql;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * a
 *
 * @author scx567888
 * @version 1.5.0
 */
public final class NamedParameterSQL {

    /**
     * 具名参数匹配的正则表达式
     */
    private static final Pattern NAMED_PARAMETER_PATTERN = Pattern.compile(":([a-zA-Z0-9_.-]+)");

    /**
     * 转换后的常规 SQL
     */
    private final String normalSQL;

    /**
     * 具名参数名称索引
     */
    private final String[] namedParameterNameIndex;


    /**
     * <p>Constructor for NamedParameterSQLConverter.</p>
     *
     * @param namedParameterSQL a {@link java.lang.String} object
     */
    public NamedParameterSQL(String namedParameterSQL) {
        var matcher = NAMED_PARAMETER_PATTERN.matcher(namedParameterSQL);
        var tempNamedParameterNameIndex = new ArrayList<String>();
        var normalSQL = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(normalSQL, "?");
            tempNamedParameterNameIndex.add(matcher.group(1));
        }
        matcher.appendTail(normalSQL);
        this.normalSQL = normalSQL.toString();
        this.namedParameterNameIndex = tempNamedParameterNameIndex.toArray(String[]::new);
    }

    /**
     * a
     *
     * @return a
     */
    public String normalSQL() {
        return normalSQL;
    }

    /**
     * a
     *
     * @return a
     */
    public String[] namedParameterNameIndex() {
        return namedParameterNameIndex;
    }

}
