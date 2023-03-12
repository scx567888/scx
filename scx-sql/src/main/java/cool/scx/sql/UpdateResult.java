package cool.scx.sql;

/**
 * 数据库更新结果
 *
 * @author scx567888
 * @version 0.0.1
 */
public record UpdateResult(long affectedItemsCount, long[] generatedKeys) {

    /**
     * 返回第一个主键
     *
     * @return 主键
     */
    public Long firstGeneratedKey() {
        return this.generatedKeys.length > 0 ? this.generatedKeys[0] : null;
    }

}