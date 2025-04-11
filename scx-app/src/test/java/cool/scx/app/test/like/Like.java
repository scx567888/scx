package cool.scx.app.test.like;

import cool.scx.app.base.BaseModel;
import cool.scx.data.jdbc.annotation.Table;

//特殊表名
@Table
public class Like extends BaseModel {

    public Order order;

}
