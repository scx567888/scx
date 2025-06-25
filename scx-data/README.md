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

用于描述查询条件的数据结构，支持树形条件、分页、排序。

您可以使用 QueryBuilder 方便的创建 Query 对象。

### FieldPolicy

用于定义字段访问策略，支持字段包含、排除、虚拟字段、表达式赋值等复杂场景。

您可以使用 FieldPolicyBuilder 方便的创建 FieldPolicy 对象。

### Aggregation

用于定义聚合操作，支持多字段分组、多种聚合函数及表达式分组。

您可以使用 AggregationBuilder 方便的创建 Aggregation 对象。

---

## Query 使用示例

### 简单示例 1：单条件查询，分页排序

```java
Query query = eq("status", 1).asc("createdAt").limit(20);
```

*说明：查询状态为1的记录，按创建时间升序排序，获取前20条数据。*

---

### 简单示例 2：多个条件与逻辑组合查询

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

*说明：查询类别为书籍，且价格大于100或者标题包含“Java”的数据，按销量倒序排序，取前10条。*

---

### 复杂示例：多层嵌套条件 + 多排序 + 分页 + BuildControl 控制

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

*说明：*

* 查询条件逻辑：

    * （状态为活跃且创建时间在2023年内）
    * 或
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

## FieldPolicy 使用示例

### 简单示例：只包含指定字段，忽略空值

```java
FieldPolicy fieldPolicy = include("id", "name", "email")
        .ignoreNull(true);
```

*说明：*

* 只包含 `id`, `name`, `email` 三个字段，其他字段全部排除。
* 插入或更新时，忽略这些字段的空值（null）不进行操作。

---

### 复杂示例：排除字段、虚拟字段、表达式赋值及插入时字段策略

```java
FieldPolicy fieldPolicy = exclude("password", "secretToken")
        .virtualField("fullName", "CONCAT(firstName, ' ', lastName)")   // 虚拟字段，不存在于数据库表
        .assignField("updatedAt", "CURRENT_TIMESTAMP")                  // 插入/更新时赋值数据库表达式
        .ignoreNull("email", true)                                      // 插入/更新时忽略空 email
        .ignoreNull("phone", false);                                    // phone 字段即使为空也写入
```

*说明：*

* `virtualField` 添加查询时返回的虚拟字段，简化客户端逻辑。
* `assignField` 通过数据库表达式自动赋值，无需客户端手动设置。
* 细粒度控制插入/更新时哪些字段忽略空值。

---

## Aggregation 使用示例

### 简单示例：按字段分组，统计计数

```java
Aggregation aggregation = groupBy("category")
        .agg("count", "COUNT(*)");
```

*说明：*

* 按 `category` 字段分组。
* 聚合计算每组的数量，别名为 `count`。

---

### 复杂示例：多字段分组，多种聚合计算，带表达式分组

```java
Aggregation aggregation = groupBy("region")
        .groupBy("month", "DATE_FORMAT(orderDate, '%Y-%m')")
        .agg("totalSales", "SUM(salesAmount)")
        .agg("avgDiscount", "AVG(discount)")
        .agg("maxOrder", "MAX(orderAmount)")
        .agg("orderCount", "COUNT(*)");
```

*说明：*

* 按 `region` 字段分组。
* 按表达式分组，按订单日期格式化为年月（`month`）。
* 聚合计算销售总额、平均折扣、最大订单金额和订单数量。

---

## Repository 调用示例

### 1. 基础查询

```java
Repository<User, Long> userRepository = ...;

Query query = eq("status", "active").limit(10);

List<User> activeUsers = userRepository.find(query);
```

*说明：查询状态为“active”的用户，最多返回10条。*

---

### 2. 查询时包含虚拟字段

```java
FieldPolicy fieldPolicy = FieldPolicyBuilder.include("id", "name", "email")
        .virtualField("fullName", "CONCAT(firstName, ' ', lastName)");

Query query = eq("role", "admin");

List<Map<String, Object>> admins = userRepository.find(query, fieldPolicy);
```

*说明：查询管理员角色，结果包含虚拟字段 `fullName`，简化客户端姓名展示。*

---

### 3. 插入时自动使用表达式赋值

```java
Repository<Product, Long> productRepository = ...;

Product newProduct = new Product();
newProduct.name = "New Gadget";
newProduct.price = 299.99;

// 字段策略：插入时 updatedAt 使用数据库当前时间
FieldPolicy fieldPolicy = assignField("updatedAt", "CURRENT_TIMESTAMP");

productRepository.add(newProduct, fieldPolicy);
```

*说明：无需客户端设置更新时间，数据库自动填充。*

---

明白了！我帮你写一个“上下文与事务管理”章节，保持风格一致，用介绍+示例的方式展现这几个接口的设计亮点和使用方式：

---

## 上下文与事务管理支持

`scx-data` 设计了完善的上下文管理与事务管理机制，支持自动与手动事务控制，帮助业务代码优雅地处理数据操作中的资源管理和事务一致性。

### 核心设计

* **上下文自动管理**：通过 `ContextManager`，数据操作可在自动管理的上下文中执行，保证资源正确打开和关闭，降低开发负担。
* **灵活事务控制**：`TransactionManager` 支持手动控制事务提交与回滚，也支持自动事务管理，遇异常自动回滚，无异常自动提交。
* **事务上下文抽象**：`TransactionContext` 负责提交和回滚操作，支持多种事务实现的灵活替换。

---

### 使用示例

#### 1. 自动上下文执行

```java
// 在自动管理的上下文中执行查询操作，无需显式打开关闭资源
String result = contextManager.autoContext(() -> {
    // 这里写数据库操作代码
    return someRepository.findSomething();
});
```

*说明：* `autoContext` 自动管理执行过程中的资源，简化异常处理和资源关闭。

---

#### 2. 手动事务控制

```java
transactionManager.withTransaction(txContext -> {
    userRepository.insert(user);
    orderRepository.insert(order);
    // 根据业务需要决定提交或回滚
    if (someCondition) {
        txContext.commit();
    } else {
        txContext.rollback();
    }
});
```

*说明：* 手动事务控制允许业务代码根据条件灵活提交或回滚事务，满足复杂业务场景。

---

#### 3. 自动事务执行

```java
transactionManager.autoTransaction(() -> {
    userRepository.update(user);
    orderRepository.update(order);
    // 发生异常时自动回滚，无异常自动提交
});
```

*说明：* 自动事务管理大幅简化事务代码，异常自动回滚，保证数据一致性，提升开发效率。

---


## 设计理念

* **灵活性**：丰富的接口设计支持多样化查询、聚合和字段策略需求。
* **惰性执行**：查询和聚合操作均延迟执行，方便底层数据源适配。
* **清晰分层**：查询条件、字段策略、聚合操作职责明确，方便扩展维护。
* **表达式支持**：字段策略和排序支持数据库表达式，简化复杂业务场景。
* **细粒度字段控制**：插入更新时可灵活控制空值忽略、表达式赋值、虚拟字段等，极大提升业务适配能力。

---

## 关于 JOIN 的设计决策

在 `scx-data` 中，我们**刻意不支持 JOIN 操作**。这一设计选择经过深思熟虑，基于以下几点考虑：

### 1. 避免数据重复与性能陷阱

JOIN 语句往往带来数据重复返回，导致结果集膨胀，查询效率急剧下降。尤其在多对多、多级关联场景中，JOIN 会造成复杂的性能瓶颈，难以优化。

### 2. 简化接口设计，增强可维护性

不支持 JOIN，意味着查询条件、聚合、字段策略等接口更加纯粹和清晰。用户能够聚焦于单表查询和操作，减少因复杂关联带来的错误和维护成本。

### 3. 业务层组合更灵活

关联数据的整合推荐由业务逻辑或服务层完成。通过多步查询或缓存机制，业务系统可灵活组合结果，既满足复杂需求，又避免了数据库层面的耦合。

### 4. 易于多数据源支持

不使用 JOIN 设计，使得 `scx-data` 更加适配分布式、多数据源和 NoSQL 场景。接口抽象不依赖关系型数据库的特性，提高了可扩展性和兼容性。

---

### 推荐的替代方案

* **多步查询：** 先查询主表数据，再根据结果做后续关联查询。
* **缓存和聚合服务层：** 通过缓存、服务聚合将关联数据拼装到业务层。
* **视图或中间表：** 数据库层面预先创建视图或汇总表，简化查询逻辑。

---

### 总结

不支持 JOIN 并非能力缺失，而是刻意设计，追求接口简洁、高效和灵活。`scx-data` 鼓励清晰的查询逻辑分层，让业务系统自由掌控关联复杂度，提升整体系统的健壮性和可维护性。

---
