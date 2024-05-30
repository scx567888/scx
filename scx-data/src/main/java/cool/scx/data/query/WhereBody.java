package cool.scx.data.query;

import cool.scx.data.Query;
import cool.scx.data.query.exception.WrongWhereTypeParamSizeException;

import static cool.scx.common.util.StringUtils.isBlank;
import static cool.scx.data.query.WhereOption.Info;

/**
 * where 封装体
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class WhereBody extends QueryLike<WhereBody> {

    private final String name;
    private final WhereType whereType;
    private final Object value1;
    private final Object value2;
    private final Info info;

    public WhereBody(String name, WhereType whereType, Object value1, Object value2, Info info) {
        //名称不能为空
        if (isBlank(name)) {
            throw new IllegalArgumentException("Where 参数错误 : 名称 不能为空 !!!");
        }
        //类型也不能为空
        if (whereType == null) {
            throw new IllegalArgumentException("Where 参数错误 : whereType 不能为空 !!!");
        }
        //校验参数 并获取有效的参数数量(不为空的) 每检测到一个有效的(不为空的) 便加 1
        var validParamSize = getValidParamSize(value1, value2);
        //类型所需的参数数量和所传的合法参数数量必须一致
        if (whereType.paramSize() != validParamSize) {
            throw new WrongWhereTypeParamSizeException(whereType);
        }
        this.name = name.trim();
        this.whereType = whereType;
        this.value1 = value1;
        this.value2 = value2;
        this.info = info;
    }

    public WhereBody(String name, WhereType whereType, Object value1, Object value2, WhereOption... options) {
        this(name, whereType, value1, value2, new Info(options));
    }

    /**
     * 校验参数 并获取有效的参数数量(不为空的) 每检测到一个有效的(不为空的) 便加 1
     *
     * @param value1 a
     * @param value2 a
     * @return a
     */
    private static int getValidParamSize(Object value1, Object value2) {
        //有效的参数数量(不为空的) 每检测到一个有效的(不为空的) 便加 1
        var validParamSize = 0;
        if (value1 != null) {
            validParamSize = validParamSize + 1;
        }
        if (value2 != null) {
            validParamSize = validParamSize + 1;
        }
        return validParamSize;
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
    public Query toQuery() {
        return new QueryImpl().where(this);
    }

}
