package cool.scx.data.query;

import static cool.scx.util.StringUtils.isBlank;

/**
 * <p>GroupByBody class.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
public record GroupByBody(String name, GroupByOption.Info info) {

    public GroupByBody(String name, GroupByOption.Info info) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("GroupBy 参数错误 : 名称 不能为空 !!!");
        }
        this.name = name.trim();
        this.info = info;
    }

}
