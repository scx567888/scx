package cool.scx.sql;

import java.util.List;

/**
 * 数据库更新结果
 *
 * @author scx567888
 * @version 1.0.10
 */
public record UpdateResult(long affectedItemsCount, List<Long> generatedKeys) {

    /**
     * a
     *
     * @return a
     */
    public Long firstGeneratedKey() {
        return this.generatedKeys.size() > 0 ? this.generatedKeys.get(0) : null;
    }

}