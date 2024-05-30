package cool.scx.data.query;

/**
 * 排序类型
 *
 * @author scx567888
 * @version 0.0.1
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

    /**
     * a
     *
     * @param orderByTypeStr a
     * @return a
     */
    public static OrderByType of(String orderByTypeStr) {
        return OrderByType.valueOf(orderByTypeStr.trim().toUpperCase());
    }

}
