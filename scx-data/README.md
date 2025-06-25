# SCX Data

scx-data 是一套数据访问层的抽象接口.

核心由 3 个 Repository 组成

| 接口                     | 职责          |
|------------------------|-------------|
| Repository             | 提供最基本的 数据操作 |
| LockableRepository     | 支持查询锁       |
| AggregatableRepository | 支持聚合        |

实现可根据拥有的能力选择性的实现

### Finder

Finder 是一个惰性数据查询器, 提供 列表查询 和 流式查询.

### Aggregator

Aggregator 一个惰性数据聚合查询器, 提供 列表查询 和 流式查询.

### Query

Query 一个查询数据结构 支持 树形条件 分页 排序.

您也可以使用 QueryBuilder 快速构建 Query 对象.

### FieldPolicy

FieldPolicy 用于表示 字段策略.

您也可以使用 FieldPolicyBuilder 快速构建 FieldPolicy 对象.

### Aggregation

Aggregation 体系 用于聚合操作定义.

您也可以使用 AggregationBuilder 快速构建 Aggregation 对象.

### 关于 JOIN

本接口不引入 join 因为 join 会导致重复数据 且很难正确组合.
