package cool.scx.jdbc.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/// 用来表示 特殊的列表形式命名 SQL 参数
/// 可以用在如 in 中
/// 注意 !!! 此参数只能用于 NamedSQL
public class NamedSQLListParameter {

    private final Collection<?> values;

    public NamedSQLListParameter(Object... values) {
        this.values = List.of(values);
    }

    public NamedSQLListParameter(Iterable<?> values) {
        var temp = new ArrayList<>();
        for (var value : values) {
            temp.add(value);
        }
        this.values = temp;
    }

    public NamedSQLListParameter(Collection<?> values) {
        this.values = values;
    }

    public Collection<?> values() {
        return values;
    }

}
