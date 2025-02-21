# Scx Ansi 库

该库提供了在控制台上打印带有颜色和样式的文本的功能。它支持基本的 ANSI 颜色、8 位颜色、背景色以及不同的样式，如加粗、斜体、下划线等。该库还自动检测操作系统是否支持 ANSI，并在 Windows 10 系统上启用 ANSI 支持。

## 使用说明

### 创建 Ansi 实例

首先，需要创建一个 `Ansi` 实例来操作颜色和样式：

```java
Ansi ansi = Ansi.ansi();
```

### 添加文本和样式

通过 `add()` 方法添加带有样式的文本。你可以使用 `AnsiColor` 和 `AnsiBackground` 来设置前景色和背景色，使用 `AnsiStyle` 来添加样式。

```java
ansi.add("这是一段带颜色的文本", AnsiColor.RED, AnsiBackground.BLACK, AnsiStyle.BOLD);
```

### 支持的颜色和样式

#### 基本颜色

- **前景色** 使用 `AnsiColor` 枚举来设置：`BLACK`, `RED`, `GREEN`, `YELLOW`, `BLUE`, `MAGENTA`, `CYAN`, `WHITE` 等。
- **背景色** 使用 `AnsiBackground` 枚举来设置：`BLACK`, `RED`, `GREEN`, `YELLOW`, `BLUE`, `MAGENTA`, `CYAN`, `WHITE` 等。

```java
Ansi.ansi()
    .red("红色文本")
    .green("绿色文本")
    .blue("蓝色文本")
    .yellow("黄色文本")
    .println();
```

#### 8 位颜色

使用 `Ansi8BitColor` 和 `Ansi8BitBackground` 来设置 8 位颜色：

```java
Ansi.ansi()
    .add("这是一段带有 8 位前景色的文本", new Ansi8BitColor(100))
    .add("这是一段带有 8 位背景色的文本", new Ansi8BitBackground(200))
    .println();
```

#### 样式

使用 `AnsiStyle` 来添加样式，例如加粗、下划线等。支持的样式有：

- `RESET` - 重置样式
- `BOLD` - 加粗
- `FAINT` - 微弱
- `ITALIC` - 斜体
- `UNDERLINE` - 下划线
- `BLINK` - 闪烁
- `REVERSE` - 反转
- `HIDDEN` - 隐藏
- `CROSSED_OUT` - 删除线
- `DOUBLE_UNDERLINE` - 双下划线
- `OVERLINE` - 上划线

```java
Ansi.ansi()
    .add("加粗文本", AnsiStyle.BOLD)
    .add("下划线文本", AnsiStyle.UNDERLINE)
    .println();
```

#### 默认颜色

如果没有指定颜色，可以使用 `defaultColor()` 方法来使用默认颜色：

```java
Ansi.ansi()
    .defaultColor("这是一段默认颜色的文本")
    .println();
```

### 输出文本

可以使用以下方法来输出带有颜色的文本：

- `print(boolean useAnsi)`: 输出文本，`useAnsi` 参数用于启用或禁用 ANSI 转义码。
- `println(boolean useAnsi)`: 输出文本并换行。
- `toString(boolean useAnsi)`: 返回带有 ANSI 转义码的字符串。

例如：

```java
Ansi.ansi()
    .red("这是一段红色文本")
    .println();
```

如果你希望禁用 ANSI 支持，可以调用 `print(false)` 或 `toString(false)`。

### Windows 10 ANSI 支持

在 Windows 10 系统上，ANSI 支持默认是禁用的。你可以通过 `AnsiHelper` 类手动启用：

```java
AnsiHelper.enableWindows10AnsiSupport();
```

`checkAnsiSupport()` 方法将自动检测是否支持 ANSI。

### AnsiItem 类

`AnsiItem` 是一个数据类，包含了需要显示的文本（`value`）和应用的 ANSI 元素（`elements`）。它会将这些元素转换为 ANSI 转义码，并构建带有样式的字符串。

```java
AnsiItem item = new AnsiItem("这是一段带样式的文本", AnsiStyle.BOLD, AnsiColor.RED);
```

`AnsiItem` 会根据启用的 ANSI 样式生成适当的转义码，并在输出时自动应用它们。
