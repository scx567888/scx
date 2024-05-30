package cool.scx.core.test.person;

import cool.scx.core.base.BaseModel;
import cool.scx.data.jdbc.annotation.Table;

@Table(tablePrefix = "scx")
public class Person extends BaseModel {
    /**
     * 关联的 汽车 ID
     */
    public Long carID;

    /**
     * 年龄
     */
    public Integer age;

    /**
     * 钱
     */
    public Long money;

    public Person setMoney(long money) {
        this.money = money;
        return this;
    }

}
