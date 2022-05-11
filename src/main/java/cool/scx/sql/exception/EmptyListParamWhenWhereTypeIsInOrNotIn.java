package cool.scx.sql.exception;


import cool.scx.sql.where.WhereType;

/**
 * 当 whereType 类型为 in 或 not in 时参数列表为空时异常
 */
public final class EmptyListParamWhenWhereTypeIsInOrNotIn extends IllegalArgumentException {

    public EmptyListParamWhenWhereTypeIsInOrNotIn(WhereType whereType) {
        super("Where 参数错误 : whereType 类型 : " + whereType + " , 参数列表长度不能为 0 !!!");
    }

}
