package cool.scx.data.test;

import cool.scx.data.field_policy.*;
import cool.scx.data.serialization.FieldPolicyDeserializer;
import cool.scx.data.serialization.FieldPolicySerializer;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

public class FieldPolicyTest {
    
    public static void main(String[] args) throws Exception {
        // 只需要运行一下即可查看结果
        testInclude();
        testExclude();
        testIncludeAll();
        testExcludeAll();
        testClearFieldNames();
        testVirtualField();
        testVirtualFieldsMultiple();
        testClearVirtualFields();
        testIgnoreNullGlobalTrue();
        testIgnoreNullGlobalFalse();
        testIgnoreNullPerField();
        testRemoveIgnoreNull();
        testClearIgnoreNulls();
        testAssignField();
        testAssignFieldsMultiple();
        testClearAssignFields();
        testMixedUse();
        testEmptyPolicy();
        testJsonRoundTrip();
        testCombo1();
        testCombo2();
        testCombo3();
        testCombo4();
        testCombo5();
        testCombo6();
        testCombo7();
        testCombo8();
        testCombo9();
        testCombo10();
    }

    @Test
    public static void testInclude() throws Exception {
        var p = FieldPolicyBuilder.include("a", "b");
        assertEquals(FilterMode.INCLUDED, p.getFilterMode());
        assertArrayEquals(new String[]{"a", "b"}, p.getFieldNames());
        testSerialization(p);
    }

    @Test
    public static void testExclude() throws Exception {
        var p = FieldPolicyBuilder.exclude("x", "y");
        assertEquals(FilterMode.EXCLUDED, p.getFilterMode());
        assertArrayEquals(new String[]{"x", "y"}, p.getFieldNames());
        testSerialization(p);
    }

    @Test
    public static void testIncludeAll() throws Exception {
        var p = FieldPolicyBuilder.includeAll();
        assertEquals(FilterMode.EXCLUDED, p.getFilterMode());
        assertArrayEquals(new String[]{}, p.getFieldNames());
        testSerialization(p);
    }

    @Test
    public static void testExcludeAll() throws Exception {
        var p = FieldPolicyBuilder.excludeAll();
        assertEquals(FilterMode.INCLUDED, p.getFilterMode());
        assertArrayEquals(new String[]{}, p.getFieldNames());
        testSerialization(p);
    }

    @Test
    public static void testClearFieldNames() throws Exception {
        var p = FieldPolicyBuilder.include("a", "b").clearFieldNames();
        assertEquals(0, p.getFieldNames().length);
        testSerialization(p);
    }

    @Test
    public static void testVirtualField() throws Exception {
        var p = FieldPolicyBuilder.include().virtualField("f1", "1+1");
        assertEquals("f1", p.getVirtualFields()[0].virtualFieldName());
        testSerialization(p);
    }

    @Test
    public static void testVirtualFieldsMultiple() throws Exception {
        var p = FieldPolicyBuilder.include().virtualFields(
                new VirtualField("f1", "1"),
                new VirtualField("f2", "2"));
        assertEquals(2, p.getVirtualFields().length);
        testSerialization(p);
    }

    @Test
    public static void testClearVirtualFields() throws Exception {
        var p = FieldPolicyBuilder.include().virtualField("f", "1+1").clearVirtualFields();
        assertEquals(0, p.getVirtualFields().length);
        testSerialization(p);
    }

    @Test
    public static void testIgnoreNullGlobalTrue() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull(true);
        assertTrue(p.getIgnoreNull());
        testSerialization(p);
    }

    @Test
    public static void testIgnoreNullGlobalFalse() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull(false);
        assertFalse(p.getIgnoreNull());
        testSerialization(p);
    }

    @Test
    public static void testIgnoreNullPerField() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull("age", true);
        assertTrue(p.getIgnoreNulls().get("age"));
        testSerialization(p);
    }

    @Test
    public static void testRemoveIgnoreNull() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull("age", true);
        p.removeIgnoreNull("age");
        assertFalse(p.getIgnoreNulls().containsKey("age"));
        testSerialization(p);
    }

    @Test
    public static void testClearIgnoreNulls() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull("a", true).ignoreNull("b", false).clearIgnoreNulls();
        assertTrue(p.getIgnoreNulls().isEmpty());
        testSerialization(p);
    }

    @Test
    public static void testAssignField() throws Exception {
        var p = FieldPolicyBuilder.include().assignField("x", "NOW()");
        assertEquals("x", p.getAssignFields()[0].fieldName());
        testSerialization(p);
    }

    @Test
    public static void testAssignFieldsMultiple() throws Exception {
        var p = FieldPolicyBuilder.include().assignFields(
                new AssignField("a", "1"),
                new AssignField("b", "2"));
        assertEquals(2, p.getAssignFields().length);
        testSerialization(p);
    }

    @Test
    public static void testClearAssignFields() throws Exception {
        var p = FieldPolicyBuilder.include().assignField("a", "1").clearAssignFields();
        assertEquals(0, p.getAssignFields().length);
        testSerialization(p);
    }

    @Test
    public static void testMixedUse() throws Exception {
        var p = FieldPolicyBuilder.include("x", "y")
                .ignoreNull("x", true)
                .assignField("z", "NOW()")
                .virtualField("v1", "2+2");
        testSerialization(p);
    }

    @Test
    public static void testEmptyPolicy() throws Exception {
        var p = FieldPolicyBuilder.include();
        testSerialization(p);
    }

    @Test
    public static void testJsonRoundTrip() throws Exception {
        var original = FieldPolicyBuilder.include("f1").ignoreNull(true).assignField("x", "expr").virtualField("v", "1+1");
        var json = FieldPolicySerializer.serializeFieldPolicyToJson(original);
        var parsed = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);
        assertEquals(original.getFilterMode(), parsed.getFilterMode());
        assertEquals(original.getFieldNames().length, parsed.getFieldNames().length);
    }

    // 更多 10 个组合测试（省略具体断言逻辑，供参考结构）
    @Test
    public static void testCombo1() throws Exception {
        testSerialization(FieldPolicyBuilder.exclude("a").ignoreNull(true));
    }

    @Test
    public static void testCombo2() throws Exception {
        testSerialization(FieldPolicyBuilder.include("a").assignField("b", "1"));
    }

    @Test
    public static void testCombo3() throws Exception {
        testSerialization(FieldPolicyBuilder.excludeAll().virtualField("v", "1"));
    }

    @Test
    public static void testCombo4() throws Exception {
        testSerialization(FieldPolicyBuilder.include().ignoreNull("a", false));
    }

    @Test
    public static void testCombo5() throws Exception {
        testSerialization(FieldPolicyBuilder.include().assignFields());
    }

    @Test
    public static void testCombo6() throws Exception {
        testSerialization(FieldPolicyBuilder.exclude().clearAssignFields());
    }

    @Test
    public static void testCombo7() throws Exception {
        testSerialization(FieldPolicyBuilder.exclude().clearVirtualFields());
    }

    @Test
    public static void testCombo8() throws Exception {
        testSerialization(FieldPolicyBuilder.exclude().clearIgnoreNulls());
    }

    @Test
    public static void testCombo9() throws Exception {
        testSerialization(FieldPolicyBuilder.include().include());
    }

    @Test
    public static void testCombo10() throws Exception {
        testSerialization(FieldPolicyBuilder.exclude().exclude());
    }

    // JSON round-trip 验证核心方法
    private static void testSerialization(FieldPolicy policy) throws Exception {
        var json = FieldPolicySerializer.serializeFieldPolicyToJson(policy);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);
        assertNotNull(newPolicy);
        assertEquals(policy.getFilterMode(), newPolicy.getFilterMode());
    }

}
