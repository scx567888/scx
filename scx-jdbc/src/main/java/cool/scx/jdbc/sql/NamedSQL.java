package cool.scx.jdbc.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/// 具名参数 SQL
///
/// @author scx567888
/// @version 0.0.1
final class NamedSQL implements SQL {

    private final String sql;
    private final Object[] params;
    private final List<Object[]> batchParams;
    private final boolean isBatch;

    NamedSQL(String namedSQL, Map<String, Object> params) {
        var parser = NormalSQLParser.parse(namedSQL);
        this.sql = parser.normalSQL();
        this.params = parser.mapToArray(params);
        this.batchParams = new ArrayList<>();
        this.isBatch = false;
    }

    NamedSQL(String namedSQL, List<Map<String, Object>> batchParams) {
        var parser = NormalSQLParser.parse(namedSQL);
        this.sql = parser.normalSQL();
        this.params = new Object[]{};
        this.batchParams = parser.mapToArray(batchParams);
        this.isBatch = true;
    }

    @Override
    public String sql() {
        return sql;
    }

    @Override
    public Object[] params() {
        return params;
    }

    @Override
    public List<Object[]> batchParams() {
        return batchParams;
    }

    @Override
    public boolean isBatch() {
        return isBatch;
    }

    private record NormalSQLParser(String normalSQL, String[] nameIndex) {

        /// 具名参数匹配的正则表达式
        private static final Pattern NAMED_SQL_PATTERN = Pattern.compile(":([\\w.-]+)");

        private static NormalSQLParser parse(String namedSQL) {
            var matcher = NAMED_SQL_PATTERN.matcher(namedSQL);
            var normalSQL = new StringBuilder();
            var tempNameIndexList = new ArrayList<String>();
            while (matcher.find()) {
                matcher.appendReplacement(normalSQL, "?");
                tempNameIndexList.add(matcher.group(1));
            }
            matcher.appendTail(normalSQL);
            return new NormalSQLParser(normalSQL.toString(), tempNameIndexList.toArray(String[]::new));
        }

        private Object[] mapToArray(Map<String, Object> objectMap) {
            var objectArray = new Object[nameIndex.length];
            for (int i = 0; i < nameIndex.length; i = i + 1) {
                objectArray[i] = objectMap.get(nameIndex[i]);
            }
            return objectArray;
        }

        private List<Object[]> mapToArray(List<Map<String, Object>> listObjectMap) {
            return listObjectMap.stream().map(this::mapToArray).toList();
        }

    }

}
