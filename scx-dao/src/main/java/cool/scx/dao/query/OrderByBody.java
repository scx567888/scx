package cool.scx.dao.query;

import cool.scx.util.StringUtils;

/**
 * OrderBy 封装体
 *
 * @author scx567888
 * @version 0.0.1
 */
public record OrderByBody(String name, OrderByType orderByType, OrderByOption.Info info) {

    public OrderByBody(String name, OrderByType orderByType, OrderByOption.Info info) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("OrderBy 参数错误 : 名称 不能为空 !!!");
        }
        if (orderByType == null) {
            throw new IllegalArgumentException("OrderBy 参数错误 : orderByType 不能为空 !!!");
        }
        this.name = name.trim();
        this.orderByType = orderByType;
        this.info = info;
    }

}
