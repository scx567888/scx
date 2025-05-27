package cool.scx.data.test;

import cool.scx.data.field_policy.*;
import cool.scx.data.serialization.FieldPolicyDeserializer;
import cool.scx.data.serialization.FieldPolicySerializer;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

public class FieldPolicySerializationTest {
    
    public static void main(String[] args) throws Exception {
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
    }

    @Test
    public static void testInclude() throws Exception {
        var p = FieldPolicyBuilder.include("a", "b");
        assertEquals(FilterMode.INCLUDED, p.getFilterMode());
        assertArrayEquals(new String[]{"a", "b"}, p.getFieldNames());

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(p.getFilterMode(), newPolicy.getFilterMode());
        assertArrayEquals(p.getFieldNames(), newPolicy.getFieldNames());
        assertEquals(p.getIgnoreNull(), newPolicy.getIgnoreNull());
        assertEquals(p.getIgnoreNulls(), newPolicy.getIgnoreNulls());
        assertEquals(p.getAssignFields().length, newPolicy.getAssignFields().length);
        assertEquals(p.getVirtualFields().length, newPolicy.getVirtualFields().length);
    }

    @Test
    public static void testExclude() throws Exception {
        var p = FieldPolicyBuilder.exclude("x", "y");
        assertEquals(FilterMode.EXCLUDED, p.getFilterMode());
        assertArrayEquals(new String[]{"x", "y"}, p.getFieldNames());

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(p.getFilterMode(), newPolicy.getFilterMode());
        assertArrayEquals(p.getFieldNames(), newPolicy.getFieldNames());
        assertEquals(p.getIgnoreNull(), newPolicy.getIgnoreNull());
        assertEquals(p.getIgnoreNulls(), newPolicy.getIgnoreNulls());
        assertEquals(p.getAssignFields().length, newPolicy.getAssignFields().length);
        assertEquals(p.getVirtualFields().length, newPolicy.getVirtualFields().length);
    }

    @Test
    public static void testIncludeAll() throws Exception {
        var p = FieldPolicyBuilder.includeAll();
        assertEquals(FilterMode.EXCLUDED, p.getFilterMode());
        assertArrayEquals(new String[]{}, p.getFieldNames());

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(p.getFilterMode(), newPolicy.getFilterMode());
        assertArrayEquals(p.getFieldNames(), newPolicy.getFieldNames());
        assertEquals(p.getIgnoreNull(), newPolicy.getIgnoreNull());
        assertEquals(p.getIgnoreNulls(), newPolicy.getIgnoreNulls());
        assertEquals(p.getAssignFields().length, newPolicy.getAssignFields().length);
        assertEquals(p.getVirtualFields().length, newPolicy.getVirtualFields().length);
    }

    @Test
    public static void testExcludeAll() throws Exception {
        var p = FieldPolicyBuilder.excludeAll();
        assertEquals(FilterMode.INCLUDED, p.getFilterMode());
        assertArrayEquals(new String[]{}, p.getFieldNames());

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(p.getFilterMode(), newPolicy.getFilterMode());
        assertArrayEquals(p.getFieldNames(), newPolicy.getFieldNames());
        assertEquals(p.getIgnoreNull(), newPolicy.getIgnoreNull());
        assertEquals(p.getIgnoreNulls(), newPolicy.getIgnoreNulls());
        assertEquals(p.getAssignFields().length, newPolicy.getAssignFields().length);
        assertEquals(p.getVirtualFields().length, newPolicy.getVirtualFields().length);
    }

    @Test
    public static void testClearFieldNames() throws Exception {
        var p = FieldPolicyBuilder.include("a", "b").clearFieldNames();
        assertEquals(0, p.getFieldNames().length);

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(p.getFilterMode(), newPolicy.getFilterMode());
        assertArrayEquals(p.getFieldNames(), newPolicy.getFieldNames());
        assertEquals(p.getIgnoreNull(), newPolicy.getIgnoreNull());
        assertEquals(p.getIgnoreNulls(), newPolicy.getIgnoreNulls());
        assertEquals(p.getAssignFields().length, newPolicy.getAssignFields().length);
        assertEquals(p.getVirtualFields().length, newPolicy.getVirtualFields().length);
    }

    @Test
    public static void testVirtualField() throws Exception {
        var p = FieldPolicyBuilder.include().virtualField("f1", "1+1");
        assertEquals("f1", p.getVirtualFields()[0].virtualFieldName());

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(1, newPolicy.getVirtualFields().length);
        assertEquals("f1", newPolicy.getVirtualFields()[0].virtualFieldName());
        assertEquals("1+1", newPolicy.getVirtualFields()[0].expression());
    }

    @Test
    public static void testVirtualFieldsMultiple() throws Exception {
        var p = FieldPolicyBuilder.include().virtualFields(
                new VirtualField("f1", "1"),
                new VirtualField("f2", "2"));

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(2, newPolicy.getVirtualFields().length);
        assertEquals("f1", newPolicy.getVirtualFields()[0].virtualFieldName());
        assertEquals("2", newPolicy.getVirtualFields()[1].expression());
    }

    @Test
    public static void testClearVirtualFields() throws Exception {
        var p = FieldPolicyBuilder.include().virtualField("f", "1+1").clearVirtualFields();

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(0, newPolicy.getVirtualFields().length);
    }

    @Test
    public static void testIgnoreNullGlobalTrue() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull(true);
        assertTrue(p.getIgnoreNull());

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertTrue(newPolicy.getIgnoreNull());
    }

    @Test
    public static void testIgnoreNullGlobalFalse() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull(false);
        assertFalse(p.getIgnoreNull());

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertFalse(newPolicy.getIgnoreNull());
    }

    @Test
    public static void testIgnoreNullPerField() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull("age", true);
        assertTrue(p.getIgnoreNulls().get("age"));

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertTrue(newPolicy.getIgnoreNulls().get("age"));
    }

    @Test
    public static void testRemoveIgnoreNull() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull("age", true);
        p.removeIgnoreNull("age");
        assertFalse(p.getIgnoreNulls().containsKey("age"));

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertFalse(newPolicy.getIgnoreNulls().containsKey("age"));
    }

    @Test
    public static void testClearIgnoreNulls() throws Exception {
        var p = FieldPolicyBuilder.ignoreNull("a", true).ignoreNull("b", false).clearIgnoreNulls();

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertTrue(newPolicy.getIgnoreNulls().isEmpty());
    }

    @Test
    public static void testAssignField() throws Exception {
        var p = FieldPolicyBuilder.include().assignField("x", "NOW()");
        assertEquals("x", p.getAssignFields()[0].fieldName());

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(1, newPolicy.getAssignFields().length);
        assertEquals("x", newPolicy.getAssignFields()[0].fieldName());
        assertEquals("NOW()", newPolicy.getAssignFields()[0].expression());
    }

    @Test
    public static void testAssignFieldsMultiple() throws Exception {
        var p = FieldPolicyBuilder.include().assignFields(
                new AssignField("a", "1"),
                new AssignField("b", "2"));

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(2, newPolicy.getAssignFields().length);
        assertEquals("a", newPolicy.getAssignFields()[0].fieldName());
        assertEquals("2", newPolicy.getAssignFields()[1].expression());
    }

    @Test
    public static void testClearAssignFields() throws Exception {
        var p = FieldPolicyBuilder.include().assignField("a", "1").clearAssignFields();

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(0, newPolicy.getAssignFields().length);
    }

    @Test
    public static void testMixedUse() throws Exception {
        var p = FieldPolicyBuilder.include("x", "y")
                .ignoreNull("x", true)
                .assignField("z", "NOW()")
                .virtualField("v1", "2+2");

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(p.getFilterMode(), newPolicy.getFilterMode());
        assertArrayEquals(p.getFieldNames(), newPolicy.getFieldNames());
        assertEquals(p.getIgnoreNulls(), newPolicy.getIgnoreNulls());
        assertEquals(p.getAssignFields().length, newPolicy.getAssignFields().length);
        assertEquals(p.getVirtualFields().length, newPolicy.getVirtualFields().length);
    }

    @Test
    public static void testEmptyPolicy() throws Exception {
        var p = FieldPolicyBuilder.include();

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(p);
        var newPolicy = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(p.getFilterMode(), newPolicy.getFilterMode());
        assertEquals(0, newPolicy.getFieldNames().length);
        assertEquals(0, newPolicy.getAssignFields().length);
        assertEquals(0, newPolicy.getVirtualFields().length);
        assertTrue(newPolicy.getIgnoreNulls().isEmpty());
    }

    @Test
    public static void testJsonRoundTrip() throws Exception {
        var original = FieldPolicyBuilder.include("f1")
                .ignoreNull(true)
                .assignField("x", "expr")
                .virtualField("v", "1+1");

        var json = FieldPolicySerializer.serializeFieldPolicyToJson(original);
        var parsed = FieldPolicyDeserializer.deserializeFieldPolicyFromJson(json);

        assertEquals(original.getFilterMode(), parsed.getFilterMode());
        assertArrayEquals(original.getFieldNames(), parsed.getFieldNames());
        assertEquals(original.getIgnoreNull(), parsed.getIgnoreNull());
        assertEquals(original.getIgnoreNulls(), parsed.getIgnoreNulls());
        assertEquals(original.getAssignFields().length, parsed.getAssignFields().length);
        assertEquals(original.getVirtualFields().length, parsed.getVirtualFields().length);
    }

}
