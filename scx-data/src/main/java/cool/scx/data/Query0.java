package cool.scx.data;

import cool.scx.data.query.GroupBy;
import cool.scx.data.query.Limit;
import cool.scx.data.query.OrderBy;
import cool.scx.data.query.Where;

public interface Query0 {

    default Where getWhere() {
        return new Where();
    }

    default GroupBy getGroupBy() {
        return new GroupBy();
    }

    default OrderBy getOrderBy() {
        return new OrderBy();
    }

    default Limit getLimit() {
        return new Limit();
    }

}
