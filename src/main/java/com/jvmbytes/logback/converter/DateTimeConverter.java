package com.jvmbytes.logback.converter;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Format datetime using {@link DateTimeFormatter},
 * it's an alternative of {@link ch.qos.logback.classic.pattern.DateConverter}.
 * <p>
 * Like {@link ch.qos.logback.core.util.CachingDateFormatter},
 * {@link DateTimeConverter} also adds a timestamp cache, but check the cache outside of synchronization,
 * and results a 20x performance optimization.
 *
 * @author wongoo
 */
public class DateTimeConverter extends ClassicConverter {

    /**
     * initial the default date converter to {@link DateTimeConverter}
     */
    public static void initialDefaultDateTimeConverter() {
        Map<String, String> defaultConverterMap = new PatternLayout().getDefaultConverterMap();
        defaultConverterMap.put("d", DateTimeConverter.class.getName());
        defaultConverterMap.put("date", DateTimeConverter.class.getName());
    }

    private static class TimestampCache {
        private long time;
        private String str;
    }

    private DateTimeFormatter dateTimeFormatter;
    private ZoneId zoneId = ZoneId.systemDefault();
    private volatile TimestampCache lastTimestampCache = new TimestampCache();

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public void start() {
        String datePattern = getFirstOption();
        if (datePattern == null) {
            datePattern = CoreConstants.ISO8601_PATTERN;
        }

        if (datePattern.equals(CoreConstants.ISO8601_STR)) {
            datePattern = CoreConstants.ISO8601_PATTERN;
        }

        dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);

        List<String> optionList = getOptionList();

        // if the option list contains a TZ option, then set it.
        if (optionList != null && optionList.size() > 1) {
            TimeZone tz = TimeZone.getTimeZone(optionList.get(1));
            zoneId = tz.toZoneId();
        }

        super.start();
    }

    @Override
    public String convert(ILoggingEvent le) {
        long timeStamp = le.getTimeStamp();
        TimestampCache cache = lastTimestampCache;
        if (cache.time == le.getTimeStamp()) {
            return cache.str;
        }

        synchronized (this) {
            cache = lastTimestampCache;
            if (timeStamp != cache.time) {
                cache = new TimestampCache();
                cache.time = timeStamp;
                cache.str = dateTimeFormatter.format(Instant.ofEpochMilli(timeStamp).atZone(zoneId));
                lastTimestampCache = cache;
            }
            return cache.str;
        }
    }
}
