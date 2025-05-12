# 增删改查

我们提供了一个统一的数据访问层抽象接口 scx-data 理论上可以兼容各种关系型或非关系型数据库(未来会进行拓展), 但目前框架只提供了一套常用的
scx-data-jdbc 实现,以下演示基于 mysql 来完成

1, 首先引入依赖

```xml
<!-- scx-data 的 JDBC 实现 -->
<dependency>
    <groupId>cool.scx</groupId>
    <artifactId>scx-data-jdbc</artifactId>
    <version>{version}</version>
</dependency>
<!-- MySQL 的驱动 -->
<dependency>
    <groupId>cool.scx</groupId>
    <artifactId>scx-jdbc-mysql</artifactId>
    <version>{version}</version>
</dependency>
```
2, 创建一个 实体类用于我们的映射
```java
public class User{
    public Long id;// ID
    public Integer age;// 年龄
    public String name;// 姓名
    public String[] favorites; // 喜欢的事物
}
```
如你所见 我们没有 get set 也无需 get set , 这也是框架针对数据访问层的一种设计取舍, 只使用字段 拒绝 冗余的 get set,
接下来让我们创建一个与其相关的 jdbcContext 你可以将其理解为 这是一个 帮助更好的处理 jdbc 的工具 
```java
var mysqlDataSource = new MysqlDataSource();
mysqlDataSource.setUrl("jdbc:mysql://localhost:3306/test");
mysqlDataSource.setUser("root");
mysqlDataSource.setPassword("password");
var jdbcContext = new JDBCContext(mysqlDataSource);
```
接下来我们需要创建一个与 user 相关的 Repository 
```java
var userRepository = new JDBCRepository<>(User.class, jdbcContext);
```
如果此时我们还没有在数据中创建相关的数据表 我们也可以使用这个帮助函数来完成
```java
var userTable = userRepository.table();
//根据 userTable 的表定义再数据库创建 对应的 表结构
SchemaHelper.fixTable(userTable, jdbcContext);
```
至此 我们已经有了一个 可以使用的 userRepository 没有任何魔法 没有任何隐式行为
接下来 让我们 进行 简单的插入
```java
var user=new User();

```
