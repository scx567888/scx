package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.Expression;
import cool.scx.data.field_policy.UpdateFieldPolicy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/// FieldPolicySerializer
///
/// @author scx567888
/// @version 0.0.1
public class UpdateFieldPolicySerializer {

    public static final UpdateFieldPolicySerializer UPDATE_FIELD_POLICY_SERIALIZER = new UpdateFieldPolicySerializer();

    public String toJson(UpdateFieldPolicy fieldPolicy) throws JsonProcessingException {
        var v = serialize(fieldPolicy);
        return ObjectUtils.jsonMapper().writeValueAsString(v);
    }

    public Map<String, Object> serialize(UpdateFieldPolicy fieldPolicy) {
        return serializeUpdateFieldPolicy(fieldPolicy);
    }

    private Map<String, Object> serializeUpdateFieldPolicy(UpdateFieldPolicy fieldPolicy) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "UpdateFieldPolicy");
        m.put("filterMode", fieldPolicy.getFilterMode());
        m.put("fieldNames", fieldPolicy.getFieldNames());
        m.put("ignoreNull", fieldPolicy.getIgnoreNull());
        m.put("ignoreNulls", fieldPolicy.getIgnoreNulls());
        m.put("expressions", serializeExpressions(fieldPolicy.getExpressions()));
        return m;
    }

    public  ArrayList<Object> serializeExpressions(Expression... expressions) {
        var s=new ArrayList<Object>();
        for (Expression expression : expressions) {
            s.add(serializeExpression(expression));
        }
        return s;
    }

    public Map<String, Object> serializeExpression(Expression expression) {
        var m=new LinkedHashMap<String, Object>();
        m.put("@type", "Expression");
        m.put("fieldName", expression.fieldName());
        m.put("expression", expression.expression());
        return m;
    }

}
