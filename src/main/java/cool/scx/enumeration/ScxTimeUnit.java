package cool.scx.enumeration;

import cool.scx.ScxHandlerR;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public enum ScxTimeUnit {

    DAY(24, Handlers.dayHandler),
    HOUR(60, Handlers.hourHandler),
    MINUTE(60, Handlers.minuteHandler),
    MONTH(31, Handlers.monthHandler),
    WEEK(7, Handlers.weekHandler),
    YEAR(12, Handlers.yearHandler);

    private final int maximum;
    private final ScxHandlerR<Integer, LocalDateTime> closestLocalDateTimeFromNowHandler;

    ScxTimeUnit(int maximum, ScxHandlerR<Integer, LocalDateTime> closestLocalDateTimeFromNowHandler) {
        this.maximum = maximum;
        this.closestLocalDateTimeFromNowHandler = closestLocalDateTimeFromNowHandler;
    }

    /**
     * 日期单位内允许的最大值
     * <p>
     * 如 Week (星期) 允许的最大值应该是 7 (表示 星期一 到 星期天)
     *
     * @return r
     */
    public int maximum() {
        return maximum;
    }

    /**
     * 根据 "索引" 获取距离此单位最近的一天
     * <p>
     * 如当前是 2021/12/14 (星期二) 索引值为 4 (表示星期四)
     * <p>
     * 那么返回值 应该是 2021/12/16
     *
     * @return s
     */
    public LocalDateTime getClosestLocalDateTimeFromNow(int index) {
        return closestLocalDateTimeFromNowHandler.handle(index);
    }

    private static class Handlers {

        //计算周的 handler
        private static final ScxHandlerR<Integer, LocalDateTime> weekHandler = (index) -> {
            var ld = LocalDate.now();
            return LocalDateTime.of(ld.with(TemporalAdjusters.next(DayOfWeek.of(index))), LocalTime.MIN);
        };

        //计算月份的 handler
        private static final ScxHandlerR<Integer, LocalDateTime> monthHandler = (index) -> {
            var ld = LocalDate.now();
            var ld1 = ld.withDayOfMonth(index);
            //当前日期小 就是这个月的
            if (ld.isBefore(ld1)) {
                return LocalDateTime.of(ld1, LocalTime.MIN);
            } else { //下个月的
                return LocalDateTime.of(ld1.plusMonths(1), LocalTime.MIN);
            }
        };

        //计算小时的 handler
        private static final ScxHandlerR<Integer, LocalDateTime> hourHandler = (index) -> {
            var ld = LocalDateTime.now();
            var ld1 = ld.withMinute(index);
            //当前分钟小 就是这个小时的
            if (ld.isBefore(ld1)) {
                return ld1.withSecond(0).withNano(0);
            } else { //下个小时
                return ld1.plusHours(1).withSecond(0).withNano(0);
            }
        };

        //计算天的 handler
        private static final ScxHandlerR<Integer, LocalDateTime> dayHandler = (index) -> {
            var ld = LocalDateTime.now();
            var ld1 = ld.withHour(index);
            //当前小时小 就是这个今天的
            if (ld.isBefore(ld1)) {
                return ld1.withMinute(0).withSecond(0).withNano(0);
            } else { //明天的
                return ld1.plusDays(1).withMinute(0).withSecond(0).withNano(0);
            }
        };

        //计算分钟的 handler
        private static final ScxHandlerR<Integer, LocalDateTime> minuteHandler = (index) -> {
            var ld = LocalDateTime.now();
            var ld1 = ld.withSecond(index);
            //当前小时小 就是这个今天的
            if (ld.isBefore(ld1)) {
                return ld1.withNano(0);
            } else { //下分钟
                return ld1.plusMinutes(1).withNano(0);
            }
        };

        //计算年的 handler
        private static final ScxHandlerR<Integer, LocalDateTime> yearHandler = (index) -> {
            var ld = LocalDate.now();
            var ld1 = ld.withMonth(index);
            //当前月份小 就是这个月
            if (ld.isBefore(ld1)) {
                return LocalDateTime.of(ld.withDayOfMonth(1), LocalTime.MIN);
            } else { //下分钟
                return LocalDateTime.of(ld1.plusYears(1).withDayOfMonth(1), LocalTime.MIN);
            }
        };

    }

}