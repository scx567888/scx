package cool.scx.data.query;

import static cool.scx.data.query.OrderByOption.Info;
import static cool.scx.util.StringUtils.isBlank;

/**
 * OrderBy 封装体
 *
 * @author scx567888
 * @version 0.0.1
 */
public record OrderByBody(String name, OrderByType orderByType, Info info) {

    public OrderByBody(String name, OrderByType orderByType, Info info) {
        if (isBlank(name)) {
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
