package cool.scx.data.query;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.query.QueryOption.Info;
import static cool.scx.data.query.QueryOption.ofInfo;

/// Where
///
/// @author scx567888
/// @version 0.0.1
public final class Where extends QueryLike<Where> {

    private final String name;
    private final WhereType whereType;
    private final Object value1;
    private final Object value2;
    private final Info info;

    public Where(String name, WhereType whereType, Object value1, Object value2, Info info) {
        //名称不能为空
        if (isBlank(name)) {
            throw new IllegalArgumentException("Where 参数错误 : 名称 不能为空 !!!");
        }
        //类型也不能为空
        if (whereType == null) {
            throw new IllegalArgumentException("Where 参数错误 : whereType 不能为空 !!!");
        }
        this.name = name;
        this.whereType = whereType;
        this.value1 = value1;
        this.value2 = value2;
        this.info = info;
    }

    public Where(String name, WhereType whereType, Object value1, Object value2, QueryOption... options) {
        this(name, whereType, value1, value2, ofInfo(options));
    }

    public String name() {
        return name;
    }

    public WhereType whereType() {
        return whereType;
    }

    public Object value1() {
        return value1;
    }

    public Object value2() {
        return value2;
    }

    public Info info() {
        return info;
    }

    @Override
    protected Query toQuery() {
        return new QueryImpl().where(this);
    }

}
