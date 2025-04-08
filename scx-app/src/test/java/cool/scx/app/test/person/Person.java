package cool.scx.app.test.person;

import cool.scx.app.base.BaseModel;
import cool.scx.data.jdbc.annotation.Table;

@Table
public class Person extends BaseModel {
    /// 关联的 汽车 ID
    public Long carID;

    /// 年龄
    public Integer age;

    /// 钱
    public Long money;

    public Person setMoney(long money) {
        this.money = money;
        return this;
    }

}
