package cool.scx.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ScheduledList.class)
public @interface Scheduled {

    String cron();

}
