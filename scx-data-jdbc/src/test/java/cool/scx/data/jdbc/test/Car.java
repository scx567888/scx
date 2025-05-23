package cool.scx.data.jdbc.test;

import cool.scx.data.jdbc.annotation.Column;
import cool.scx.data.jdbc.annotation.Table;

@Table
public class Car {

    /// id
    @Column(primary = true, autoIncrement = true)
    public Long id;
    
    public String name;

    @Column(columnName = "sIzE")
    public Integer size;
    
    public String color;
    
    @Column(columnName = "CITY")
    public String city;
    
}
