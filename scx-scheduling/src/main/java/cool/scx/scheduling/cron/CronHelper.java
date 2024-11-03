package cool.scx.scheduling.cron;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static cool.scx.scheduling.cron.Type.*;

public final class CronHelper {

    //用来匹配 ?-? 这种域表达式
    private static final Pattern RANGE_PATTERN = Pattern.compile("^(\\d+)-(\\d+)$");
    //用来匹配 ?/? 这种步进表达式
    private static final Pattern STEP_PATTERN = Pattern.compile("^[^/]+/[^/]+$");

    private static int[] createByRange(int start, int end, int step) {
        //步进值 为 1 直接调用 createByRange
        if (step == 1) {
            return createByRange(start, end);
        }
        //如果不可能步进 返回空数组 
        if (step > end - start) {
            return new int[]{};
        }
        var values = new ArrayList<Integer>();
        for (int i = start; i <= end; i += step) {
            values.add(i);
        }
        return values.stream().mapToInt(i -> i).toArray();
    }

    private static int[] createByRange(int start, int end) {
        var values = new ArrayList<Integer>();
        for (var i = start; i <= end; i++) {
            values.add(i);
        }
        return values.stream().mapToInt(i -> i).toArray();
    }

    public static ParseResult parseList(String segment, int min, int max) {
        var parts = segment.split(",");
        var valueList = new ArrayList<Integer>();

        for (var part : parts) {
            var partResult = parseSegment(part, min, max);
            var partValues = partResult.values;
            for (var value : partValues) {
                valueList.add(value);
            }
        }
        //这里不需要排序 我们会统一排序
        var values = valueList.stream().mapToInt((i) -> i).toArray();
        return new ParseResult(LIST, values);
    }

    public static ParseResult parseStep(String segment, int min, int max) {
        var stepParts = segment.split("/");
        var baseValues = parseSegment(stepParts[0], min, max);
        var step = Integer.parseInt(stepParts[1]);
        if (step <= 0) {
            throw new IllegalArgumentException("步进值不能为 0: " + segment);
        }
        int start;
        int end;
        if (baseValues.type == VALUE) {
            start = baseValues.values[0];
            end = max;
        } else {
            start = baseValues.values[0];
            end = baseValues.values[baseValues.values.length - 1];
        }

        var values = createByRange(start, end, step);
        if (values.length == 0) {
            throw new IllegalArgumentException("不存在可步进区间 : " + segment);
        }
        return new ParseResult(STEP, values);
    }

    public static ParseResult parseRange(String segment, int min, int max) {
        var rangeParts = segment.split("-");
        var start = Integer.parseInt(rangeParts[0]);
        var end = Integer.parseInt(rangeParts[1]);
        if (start < min || end > max) {
            throw new IllegalArgumentException("值错误 :" + segment + ". 必须在范围区间 [" + min + " - " + max + "]");
        }
        if (start > end) {
            throw new IllegalArgumentException("值错误 :" + segment + ". 起始值不能大于终结值 ");
        }
        var values = createByRange(start, end);
        return new ParseResult(RANGE, values);
    }

    public static ParseResult parseAny(String segment, int min, int max) {
        var values = createByRange(min, max);
        return new ParseResult(ANY, values);
    }

    public static ParseResult parseValue(String segment, int min, int max) {
        int value;
        try {
            value = Integer.parseInt(segment);
        } catch (NumberFormatException var5) {
            throw new IllegalArgumentException("值错误 : " + segment + " . 不是一个正确的表达式");
        }

        if (value < min || value > max) {
            throw new IllegalArgumentException("值错误 :" + segment + ". 必须在范围区间 [" + min + " - " + max + "]");
        }
        return new ParseResult(VALUE, value);
    }

    public static ParseResult parseSegment(String segment, int min, int max) {
        if (segment.contains(",")) {
            return parseList(segment, min, max);
        } else if (STEP_PATTERN.matcher(segment).matches()) {
            return parseStep(segment, min, max);
        } else if (RANGE_PATTERN.matcher(segment).matches()) {
            return parseRange(segment, min, max);
        } else if (segment.equals("*")) {
            return parseAny(segment, min, max);
        } else {
            return parseValue(segment, min, max);
        }
    }

    public static int[] normalize(int[] values) {
        //去重并排序
        return IntStream.of(values).distinct().sorted().toArray();
    }

}
