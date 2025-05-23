# 拓展原则 (SPI vs 显式配置)

SCX 根据扩展对系统行为的影响程度，采用两种不同的处理方式：

| 扩展类型    | 处理方式     | 原因                   |
|:--------|:---------|:---------------------|
| 系统行为决策点 | 显式配置     | 必须明确控制，保证系统的确定性和可预测性 |
| 可选扩展能力  | SPI 自动发现 | 动态适配，提升灵活性，不影响主逻辑    |

## 为什么要区分处理方式？

* **系统行为决策**（如协议升级处理器）直接决定请求流转和核心流程，
  → 必须由开发者**显式声明和配置**，避免隐式行为导致不确定性。

* **可选扩展能力**（如数据库方言）属于增强特性，不影响系统核心行为，
  → 可以通过 **SPI 自动发现**，让扩展自然适配，提升开发体验。

## 示例

* 在 `scx-http-x` 中，**HTTP 协议升级**需要显式配置 `UpgradeHandler`，因为协议变化会直接影响服务器处理逻辑。

* 在 `scx-jdbc` 中，**数据库方言**通过 SPI 动态加载，因为不同方言仅影响具体数据库交互，不改变主流程。

---
