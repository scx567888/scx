package cool.scx.sql;

import java.util.List;

/**
 * 数据库更新结果
 *
 * @author scx567888
 * @version 1.0.10
 */
public record UpdateResult(long affectedItemsCount, List<Long> generatedKeys) {

}