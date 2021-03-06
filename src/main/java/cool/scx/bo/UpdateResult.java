package cool.scx.bo;

import java.util.List;

/**
 * 数据库更新结果
 *
 * @author scx567888
 * @version 1.0.10
 */
public final class UpdateResult {

    /**
     * 受影响的行数
     */
    public final long affectedLength;

    /**
     * 主键 id 集合
     */
    public final List<Long> generatedKeys;

    /**
     * c
     *
     * @param affectedLength a {@link java.lang.Integer} object.
     * @param generatedKeys  a {@link java.util.List} object.
     */
    public UpdateResult(long affectedLength, List<Long> generatedKeys) {
        this.affectedLength = affectedLength;
        this.generatedKeys = generatedKeys;
    }

}
