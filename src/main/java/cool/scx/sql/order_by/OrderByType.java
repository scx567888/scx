package cool.scx.sql.order_by;

/**
 * 排序类型
 *
 * @author scx567888
 * @version 0.3.6
 */
public enum OrderByType {

    /**
     * 正序 : 也就是从小到大 (1,2,3,4,5,6)
     */
    ASC,

    /**
     * 倒序 : 也就是从大到小 (6,5,4,3,2,1)
     */
    DESC;

    public static OrderByType of(String orderByStr) {
        if ("ASC".equalsIgnoreCase(orderByStr.trim())) {
            return OrderByType.ASC;
        } else if ("DESC".equalsIgnoreCase(orderByStr.trim())) {
            return OrderByType.DESC;
        } else {
            throw new IllegalArgumentException("排序类型有误 : 只能是 asc 或 desc (不区分大小写) !!!");
        }
    }

}
