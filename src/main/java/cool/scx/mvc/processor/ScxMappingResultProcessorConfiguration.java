package cool.scx.mvc.processor;

import cool.scx.mvc.processor.impl.BaseVoResultProcessor;
import cool.scx.mvc.processor.impl.CanConvertJsonResultProcessor;
import cool.scx.mvc.processor.impl.CanConvertStringResultProcessor;
import cool.scx.mvc.processor.impl.NullResultProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * a
 */
public final class ScxMappingResultProcessorConfiguration {

    private static final List<ScxMappingResultProcessor> SCX_MAPPING_RESULT_PROCESSOR_LIST = new ArrayList<>();

    static {
        //初始化默认的处理器 (注意顺序)
        addResultProcessor(new NullResultProcessor());
        addResultProcessor(new BaseVoResultProcessor());
        addResultProcessor(new CanConvertStringResultProcessor());
        addResultProcessor(new CanConvertJsonResultProcessor());
    }

    /**
     * a
     *
     * @param resultProcessor a
     */
    public static void addResultProcessor(ScxMappingResultProcessor resultProcessor) {
        SCX_MAPPING_RESULT_PROCESSOR_LIST.add(resultProcessor);
    }

    /**
     * a
     *
     * @param result a
     * @return a
     */
    public static ScxMappingResultProcessor findResultProcessor(Object result) {
        for (var scxMappingResultProcessor : SCX_MAPPING_RESULT_PROCESSOR_LIST) {
            if (scxMappingResultProcessor.canHandle(result)) {
                return scxMappingResultProcessor;
            }
        }
        throw new IllegalArgumentException("未找到任何合适的 ScxMappingResultProcessor !!!" + result);
    }

}
