# SCX Data

`scx-data` 是一套数据访问层的抽象接口库，旨在提供灵活、可扩展且易用的数据查询和聚合能力。

## 核心接口

核心由三类 Repository 组成，支持不同的数据操作能力：

| 接口                       | 职责               |
|--------------------------|------------------|
| `Repository`             | 提供最基础的数据操作（CRUD） |
| `LockableRepository`     | 支持查询锁定，保证并发控制    |
| `AggregatableRepository` | 支持复杂的聚合查询        |

实现类可根据具体能力，选择性实现对应接口。

---

## 主要组件简介

### Query

`Query` 是描述查询条件的数据结构，支持树形条件、分页、排序。

您可以使用 QueryBuilder 方便的创建 Query 对象

## 简单示例 1：单条件查询，分页排序

```java
Query query = eq("status", 1).asc("createdAt").limit(20);
```

* 说明：查询状态为1的记录，按创建时间升序排序，获取前20条数据。

---

## 简单示例 2：多个条件与逻辑组合查询

```java
Query query =
        and(
                eq("category", "books"),
                or(
                        gt("price", 100),
                        like("title", "%Java%")
                )
        )
                .orderBy(QueryBuilder.desc("sales"))
                .limit(10);
```

* 说明：查询类别为书籍，且价格大于100或者标题包含“Java”的数据，按销量倒序排序，取前10条。

---

## 超级复杂示例：多层嵌套条件 + 多排序 + 分页 + BuildControl 控制

```java
Query query = query()
        .where(or(
                and(
                        eq("status", "active", BuildControl.SKIP_IF_NULL),
                        between("createdAt", "2023-01-01", "2023-12-31")
                ),
                and(
                        ne("status", "deleted"),
                        or(
                                in("type", List.of("premium", "vip")),
                                like("description", "%exclusive%", BuildControl.SKIP_IF_EMPTY_STRING)
                        ),
                        not(
                                eq("region", "restricted")
                        )
                )
        ))
        .orderBys(
                asc("priority"),
                desc("lastUpdated", BuildControl.USE_EXPRESSION)
        )
        .limit(50)
        .offset(100);
```

* 说明：

    * 查询条件逻辑：

        * （状态为活跃且创建时间在2023年内）
          或
        * （状态不等于“deleted”且（类型是“premium”或“vip”或者描述包含“exclusive”）且区域不为“restricted”）

    * 排序：

        * 优先级升序
        * 按最后更新时间倒序（字段使用表达式）

    * 分页：

        * 跳过前100条，取50条结果

    * `BuildControl` 作用：

        * `SKIP_IF_NULL` 跳过空状态参数
        * `SKIP_IF_EMPTY_STRING` 跳过空字符串描述条件
        * `USE_EXPRESSION` 表达式排序，不做额外字段转换

---

### FieldPolicy

`FieldPolicy` 用于定义字段访问策略：

您可以使用 FieldPolicyBuilder 方便的创建 FieldPolicy 对象

## 简单示例：只包含指定字段，忽略空值

```java
FieldPolicy fieldPolicy = include("id", "name", "email")
        .ignoreNull(true);
```

* 说明：

    * 只包含 `id`, `name`, `email` 三个字段，其他字段全部排除。
    * 插入或更新时，忽略这些字段的空值（null）不进行操作。

---

## 复杂示例：排除某些字段，增加虚拟字段，指定字段表达式和单独忽略空值

```java
FieldPolicy fieldPolicy = exclude("password", "secretToken")
        .virtualField("fullName", "CONCAT(firstName, ' ', lastName)")
        .assignField("updatedAt", "CURRENT_TIMESTAMP")
        .ignoreNull("email", true)
        .ignoreNull("phone", false);
```

* 说明：

    * 排除 `password` 和 `secretToken` 字段，不包含它们。
    * 添加一个虚拟字段 `fullName`，其表达式为数据库拼接 `firstName` 和 `lastName`。
    * 对字段 `updatedAt` 赋值为数据库表达式 `CURRENT_TIMESTAMP`（通常用于更新时间）。
    * 对 `email` 字段设置忽略空值（如果为空则不更新该字段）。
    * 对 `phone` 字段不忽略空值（即使为空，也会更新该字段为 null）。


---

### Aggregation

`Aggregation` 用于定义聚合操作：

您可以使用 AggregationBuilder 方便的创建 Aggregation 对象

## 简单示例：按字段分组，统计计数

```java
Aggregation aggregation = groupBy("category")
        .agg("count", "COUNT(*)");
```

* 说明：

    * 按 `category` 字段进行分组。
    * 聚合计算每组的数量，别名为 `count`。

---

## 复杂示例：多字段分组，多种聚合计算，带表达式分组

```java
Aggregation aggregation = groupBy("region")
        .groupBy("month", "DATE_FORMAT(orderDate, '%Y-%m')")
        .agg("totalSales", "SUM(salesAmount)")
        .agg("avgDiscount", "AVG(discount)")
        .agg("maxOrder", "MAX(orderAmount)")
        .agg("orderCount", "COUNT(*)");
```

* 说明：

    * 按 `region` 字段分组。
    * 按表达式分组，按订单日期格式化为年月（`month`）。
    * 聚合计算：

        * `totalSales`：销售金额总和。
        * `avgDiscount`：平均折扣。
        * `maxOrder`：最大订单金额。
        * `orderCount`：订单数量。

---

## 设计理念

* **灵活性**：丰富的接口设计支持多样化查询和聚合需求
* **惰性执行**：查询和聚合操作均延迟执行，方便底层数据源适配
* **清晰分层**：查询条件、字段策略、聚合操作职责明确
* **无 JOIN 设计**：接口刻意不支持 JOIN，避免重复数据和复杂合并，推荐用业务逻辑或多步查询处理关联

---
