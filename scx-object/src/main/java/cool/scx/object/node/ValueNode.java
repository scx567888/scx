package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

/// ValueNode
///
/// @author scx567888
/// @version 0.0.1
public sealed interface ValueNode extends Node permits NumberNode, TextNode, BooleanNode {

    /// 转换为 int (不保证精度, 可能会发生截断)
    ///
    /// @return int
    /// @throws NumberFormatException 数字格式不正确 (一般出现在 TextNode 中)
    int asInt() throws NumberFormatException;

    /// 转换为 long (不保证精度, 可能会发生截断)
    ///
    /// @return long
    /// @throws NumberFormatException 数字格式不正确 (一般出现在 TextNode 中)
    long asLong() throws NumberFormatException;

    /// 转换为 float (不保证精度, 可能会发生截断)
    ///
    /// @return float
    /// @throws NumberFormatException 数字格式不正确 (一般出现在 TextNode 中)
    float asFloat() throws NumberFormatException;

    /// 转换为 double (不保证精度, 可能会发生截断)
    ///
    /// @return double
    /// @throws NumberFormatException 数字格式不正确 (一般出现在 TextNode 中)
    double asDouble() throws NumberFormatException;

    /// 转换为 BigInteger (不保证精度, 可能会发生截断)
    ///
    /// @return BigInteger
    /// @throws NumberFormatException 数字格式不正确 (一般出现在 TextNode 中)
    BigInteger asBigInteger() throws NumberFormatException;

    /// 转换为 BigDecimal
    ///
    /// @return BigDecimal
    /// @throws NumberFormatException 数字格式不正确 (一般出现在 TextNode 中)
    BigDecimal asBigDecimal() throws NumberFormatException;

    /// 转换为 String
    ///
    /// @return String
    String asText();

    /// 转换为 boolean
    ///
    /// 对于 数值类型 value != 0
    /// 对于 文本类型 "true".equalsIgnoreCase(s)
    ///
    /// @return boolean
    boolean asBoolean();

    @Override
    ValueNode deepCopy();

}
