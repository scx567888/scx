package cool.scx.data.query;

import cool.scx.data.query.exception.WrongWhereTypeParamSizeException;

import static cool.scx.util.StringUtils.isBlank;

/**
 * where 封装体
 *
 * @param name a
 * @author scx567888
 * @version 0.0.1
 */
public record WhereBody(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {

    public WhereBody(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
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

}
