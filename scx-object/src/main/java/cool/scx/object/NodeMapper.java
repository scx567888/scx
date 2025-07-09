package cool.scx.object;

import cool.scx.object.node.*;
import cool.scx.reflect.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;

public class NodeMapper {

    public Node valueToNode(Object value) {
        // 空 直接 返回 NULL
        if (value == null) {
            return NullNode.NULL;
        }
        // 本身就是 Node 直接返回自身
        if (value instanceof Node n) {
            return n;
        }

        //获取 type 进行后续判断
        var type = ScxReflect.getType(value.getClass());

        switch (type) {
            //是基本类型
            case PrimitiveTypeInfo p -> {
                return primitiveValueToNode(value, p);
            }
            //是数组
            case ArrayTypeInfo t -> {
                return arrayValueToNode(value, this);
            }
            //普通类
            case ClassInfo c -> {
                //字符串 
                if (value instanceof String s) {
                    return new TextNode(s);
                }

                //常用数值类型
                if (value instanceof Byte b) {
                    return new IntNode(b);
                }

                if (value instanceof Short b) {
                    return new IntNode(b);
                }

                if (value instanceof Integer b) {
                    return new IntNode(b);
                }

                if (value instanceof Long b) {
                    return new LongNode(b);
                }

                if (value instanceof Float b) {
                    return new FloatNode(b);
                }

                if (value instanceof Double b) {
                    return new DoubleNode(b);
                }

                if (value instanceof Boolean b) {
                    return BooleanNode.of(b);
                }

                if (value instanceof Character b) {
                    return new TextNode(b.toString());
                }

                if (value instanceof BigInteger b) {
                    return new BigIntegerNode(b);
                }

                if (value instanceof BigDecimal b) {
                    return new BigDecimalNode(b);
                }

                //需要特殊处理两类 Map 和 迭代器 
                if (value instanceof Map<?, ?> map) {
                    var objectNode = new ObjectNode();
                    for (var entry : map.entrySet()) {
                        var k = entry.getKey();
                        var v = entry.getValue();
                        // todo key 可能是 null 
                        objectNode.put(k.toString(), valueToNode(v));
                    }
                    return objectNode;
                }
                //迭代器
                if (value instanceof Iterable<?> iterable) {
                    var arrayNode = new ArrayNode();
                    for (var o : iterable) {
                        arrayNode.add(valueToNode(o));
                    }
                    return arrayNode;
                }
                //迭代器
                if (value instanceof Iterator<?> iterator) {
                    var arrayNode = new ArrayNode();
                    while (iterator.hasNext()) {
                        arrayNode.add(valueToNode(iterator.next()));
                    }
                    return arrayNode;
                }

                return beanValueToTree(value, c, this);
            }
        }
    }

    public Node primitiveValueToNode(Object value, PrimitiveTypeInfo p) {
        var pClass = p.primitiveClass();
        if (pClass == byte.class) {
            return new IntNode((byte) value);
        } else if (pClass == short.class) {
            return new IntNode((short) value);
        } else if (pClass == int.class) {
            return new IntNode((int) value);
        } else if (pClass == long.class) {
            return new LongNode((long) value);
        } else if (pClass == float.class) {
            return new FloatNode((float) value);
        } else if (pClass == double.class) {
            return new DoubleNode((double) value);
        } else if (pClass == boolean.class) {
            return BooleanNode.of((boolean) value);
        } else if (pClass == char.class) {
            return new TextNode(String.valueOf((char) value));
        }
        throw new RuntimeException("Unsupported primitive type: " + pClass);
    }

    public Node arrayValueToNode(Object value, NodeMapper nodeMapper) {
        var arrayNode = new ArrayNode();
        if (value instanceof Object[] objectArr) {
            for (Object o : objectArr) {
                arrayNode.add(nodeMapper.valueToNode(o));
            }
        } else {
            switch (value) {
                case byte[] arr -> {
                    for (var b : arr) {
                        arrayNode.add(new IntNode(b));
                    }
                }
                case short[] arr -> {
                    for (var b : arr) {
                        arrayNode.add(new IntNode(b));
                    }
                }
                case int[] arr -> {
                    for (var b : arr) {
                        arrayNode.add(new IntNode(b));
                    }
                }
                case long[] arr -> {
                    for (var b : arr) {
                        arrayNode.add(new LongNode(b));
                    }
                }
                case float[] arr -> {
                    for (var b : arr) {
                        arrayNode.add(new FloatNode(b));
                    }
                }
                case double[] arr -> {
                    for (var b : arr) {
                        arrayNode.add(new DoubleNode(b));
                    }
                }
                case boolean[] arr -> {
                    for (var b : arr) {
                        arrayNode.add(BooleanNode.of(b));
                    }
                }
                case char[] arr -> {
                    for (var b : arr) {
                        arrayNode.add(new TextNode(String.valueOf(b)));
                    }
                }
                default -> throw new IllegalStateException("错误值 : " + value);
            }
        }
        return arrayNode;
    }

    public ObjectNode beanValueToTree(Object value, ClassInfo classInfo, NodeMapper mapper) {
        var objectNode = new ObjectNode();
        var allFields = classInfo.allFields();
        for (var fieldInfo :  allFields) {
            //只处理 public 非静态字段
            if (fieldInfo.accessModifier() != AccessModifier.PUBLIC || fieldInfo.isStatic()) {
                continue;
            }
            try {
                var fieldValue = fieldInfo.get(value);
                var node = mapper.valueToNode(fieldValue);
                //因为 allFields 是按照 子类 -> 父类 -> 父类的父类
                //这其中可能出现 重名字段 所以我们 putIfAbsent 以保证子类的同名字段可以覆盖父类的
                objectNode.putIfAbsent(fieldInfo.name(), node);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return objectNode;
    }

    //todo 待完成
    public <T> T nodeToValue(Node node, TypeInfo type) {
        return null;
    }

}
