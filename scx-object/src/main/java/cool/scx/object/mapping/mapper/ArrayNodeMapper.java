package cool.scx.object.mapping.mapper;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.reflect.ArrayTypeInfo;

/// ArrayNodeMapper
///
/// 支持 基本类型数组和 Object[]
///
/// 支持 单值包裹为 单值数组
///
/// @author scx567888
/// @version 0.0.1
public final class ArrayNodeMapper implements NodeMapper<Object> {

    private final ArrayTypeInfo arrayTypeInfo;
    private final NodeMapper<Object> componentNodeMapper;

    public ArrayNodeMapper(ArrayTypeInfo arrayTypeInfo, NodeMapper<Object> componentNodeMapper) {
        this.arrayTypeInfo = arrayTypeInfo;
        // 这个只能用于 fromNode, 因为 toNode 有可能是 Object[]
        this.componentNodeMapper = componentNodeMapper;
    }

    @Override
    public Node toNode(Object value, ToNodeContext context) throws NodeMappingException {
        switch (value) {
            // 处理 Object[]
            case Object[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                var i = 0;
                for (var a : arr) {
                    // 我们不能确定 每个数组元素一定是 componentNodeMapper 支持的类型
                    // 所以这里 需要 递归调用 context.toNode
                    // 但是对于基本类型则不需要
                    arrayNode.add(context.toNode(a, i));
                    i = i + 1;
                }
                return arrayNode;
            }
            case byte[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(componentNodeMapper.toNode(a, context));
                }
                return arrayNode;
            }
            case short[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(componentNodeMapper.toNode(a, context));
                }
                return arrayNode;
            }
            case int[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(componentNodeMapper.toNode(a, context));
                }
                return arrayNode;
            }
            case long[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(componentNodeMapper.toNode(a, context));
                }
                return arrayNode;
            }
            case float[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(componentNodeMapper.toNode(a, context));
                }
                return arrayNode;
            }
            case double[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(componentNodeMapper.toNode(a, context));
                }
                return arrayNode;
            }
            case boolean[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(componentNodeMapper.toNode(a, context));
                }
                return arrayNode;
            }
            case char[] arr -> {
                var arrayNode = new ArrayNode(arr.length);
                for (var a : arr) {
                    arrayNode.add(componentNodeMapper.toNode(a, context));
                }
                return arrayNode;
            }
            // 不是数组类型, 这里基本上是不可达的, 如果不是外部直接调用的话
            default -> throw new NodeMappingException("Unsupported type: " + value.getClass());
        }
    }

    @Override
    public Object fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 先处理 ArrayNode 类型
        if (node instanceof ArrayNode arrayNode) {
            // 这里需要特殊循环处理 
            var array = arrayTypeInfo.newArray(arrayNode.size());

            switch (array) {
                case Object[] result -> {
                    var i = 0;
                    for (var e : arrayNode) {
                        result[i] = componentNodeMapper.fromNode(e, context);
                        i = i + 1;
                    }
                    return result;
                }
                case byte[] result -> {
                    var i = 0;
                    for (var e : arrayNode) {
                        result[i] = (byte) componentNodeMapper.fromNode(e, context);
                        i = i + 1;
                    }
                    return result;
                }
                case short[] result -> {
                    var i = 0;
                    for (var e : arrayNode) {
                        result[i] = (short) componentNodeMapper.fromNode(e, context);
                        i = i + 1;
                    }
                    return result;
                }
                case int[] result -> {
                    var i = 0;
                    for (var e : arrayNode) {
                        result[i] = (int) componentNodeMapper.fromNode(e, context);
                        i = i + 1;
                    }
                    return result;
                }
                case long[] result -> {
                    var i = 0;
                    for (var e : arrayNode) {
                        result[i] = (long) componentNodeMapper.fromNode(e, context);
                        i = i + 1;
                    }
                    return result;
                }
                case float[] result -> {
                    var i = 0;
                    for (var e : arrayNode) {
                        result[i] = (float) componentNodeMapper.fromNode(e, context);
                        i = i + 1;
                    }
                    return result;
                }
                case double[] result -> {
                    var i = 0;
                    for (var e : arrayNode) {
                        result[i] = (double) componentNodeMapper.fromNode(e, context);
                        i = i + 1;
                    }
                    return result;
                }
                case boolean[] result -> {
                    var i = 0;
                    for (var e : arrayNode) {
                        result[i] = (boolean) componentNodeMapper.fromNode(e, context);
                        i = i + 1;
                    }
                    return result;
                }
                case char[] result -> {
                    var i = 0;
                    for (var e : arrayNode) {
                        result[i] = (char) componentNodeMapper.fromNode(e, context);
                        i = i + 1;
                    }
                    return result;
                }
                // 这里应该不可达
                default -> throw new NodeMappingException("Unsupported type: " + array.getClass());
            }
        }
        //3, 尝试宽容处理 单值包裹为 单值数组
        var array = arrayTypeInfo.newArray(1);

        switch (array) {
            case Object[] result -> {
                result[0] = componentNodeMapper.fromNode(node, context);
                return result;
            }
            case byte[] result -> {
                result[0] = (byte) componentNodeMapper.fromNode(node, context);
                return result;
            }
            case short[] result -> {
                result[0] = (short) componentNodeMapper.fromNode(node, context);
                return result;
            }
            case int[] result -> {
                result[0] = (int) componentNodeMapper.fromNode(node, context);
                return result;
            }
            case long[] result -> {
                result[0] = (long) componentNodeMapper.fromNode(node, context);
                return result;
            }
            case float[] result -> {
                result[0] = (float) componentNodeMapper.fromNode(node, context);
                return result;
            }
            case double[] result -> {
                result[0] = (double) componentNodeMapper.fromNode(node, context);
                return result;
            }
            case boolean[] result -> {
                result[0] = (boolean) componentNodeMapper.fromNode(node, context);
                return result;
            }
            case char[] result -> {
                result[0] = (char) componentNodeMapper.fromNode(node, context);
                return result;
            }
            // 这里应该不可达
            default -> throw new NodeMappingException("Unsupported type: " + array.getClass());
        }
    }

}
