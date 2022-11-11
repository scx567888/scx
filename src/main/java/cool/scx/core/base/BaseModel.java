package cool.scx.core.base;

import cool.scx.core.annotation.Column;

import java.time.LocalDateTime;

/**
 * 最基本的 model 包含最基础的元数据
 *
 * @author scx567888
 * @version 0.3.6
 */
public abstract class BaseModel {

    /**
     * id
     */
    @Column(primaryKey = true, autoIncrement = true)
    public Long id;

    /**
     * 创建时间
     */
    @Column(notNull = true, defaultValue = "CURRENT_TIMESTAMP", needIndex = true)
    public LocalDateTime dateCreated;

    /**
     * 修改时间
     */
    @Column(notNull = true, defaultValue = "CURRENT_TIMESTAMP", onUpdateValue = "CURRENT_TIMESTAMP", needIndex = true)
    public LocalDateTime dateModified;

}
