package cool.scx.app.test.car;

import cool.scx.app.base.BaseModel;
import cool.scx.data.jdbc.annotation.Column;
import cool.scx.data.jdbc.annotation.NoColumn;
import cool.scx.data.jdbc.annotation.Table;

@Table
public class Car extends BaseModel {

    @Column(unique = true)
    public String name;

    public CarColor color;

    public CarOwner owner;

    public String[] tags;
    
    //测试虚拟字段
    @NoColumn
    public String reverseName;

}
