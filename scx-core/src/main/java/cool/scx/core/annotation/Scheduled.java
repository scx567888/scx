package cool.scx.core.annotation;

import java.util.concurrent.TimeUnit;

//todo 写一个代替品
public @interface Scheduled {
    String CRON_DISABLED = "-";

    String cron() default "";

    String zone() default "";

    long fixedRate() default -1L;

    String fixedRateString() default "";

    long fixedDelay() default -1L;

    String fixedDelayString() default "";

    long initialDelay() default -1L;

    String initialDelayString() default "";

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    String scheduler() default "";
}
