package com.jvmbytes.logback.converter;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@State(Scope.Benchmark)
public class BenchmarkTimeFormatTest {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //    Benchmark                                               Mode  Cnt           Score           Error  Units
    //    BenchmarkTimeFormatTest.benchmarkDateCompare           thrpt    5  2207189500.462 ± 586260397.621  ops/s
    //    BenchmarkTimeFormatTest.benchmarkLocalDateTimeCompare  thrpt    5  1469616103.827 ±  96739669.801  ops/s
    //    BenchmarkTimeFormatTest.benchmarkDateFormat            thrpt    5     1858890.189 ±    397584.637  ops/s
    //    BenchmarkTimeFormatTest.benchmarkDateFormatThreadSafe  thrpt    5     3157124.918 ±    591312.098  ops/s
    //    BenchmarkTimeFormatTest.benchmarkLocalDateTimeFormat   thrpt    5    14177271.086 ±   1511782.889  ops/s
    //    BenchmarkTimeFormatTest.benchmarkDateParse             thrpt    5      672082.469 ±     53688.408  ops/s
    //    BenchmarkTimeFormatTest.benchmarkDateParseThreadSafe   thrpt    5     2237964.173 ±    368861.922  ops/s
    //    BenchmarkTimeFormatTest.benchmarkLocalDateTimeParse    thrpt    5     5963536.961 ±   1817270.394  ops/s
    public static void main(String[] args) throws RunnerException {
        Options options =
            new OptionsBuilder().include(BenchmarkTimeFormatTest.class.getSimpleName()).forks(1).warmupIterations(1)
                .threads(4).build();
        new Runner(options).run();
    }

    Date startTime;
    Date endTime;

    LocalDateTime startDateTime;
    LocalDateTime endDateTime;

    DateTimeFormatter timeFormatter;
    SimpleDateFormat simpleDateFormat;

    @Setup
    public void initTest() throws ParseException {
        timeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);

        startTime = simpleDateFormat.parse("2020-11-01 00:00:00");
        endTime = simpleDateFormat.parse("2020-11-01 23:59:59");

        startDateTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        endDateTime = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

    }

    public void bool(boolean b) {
    }

    public void str(String s) {
    }

    public void date(Date d) {
    }

    public void dateTime(LocalDateTime d) {
    }

    @Benchmark
    public void benchmarkDateCompare() {
        bool(startTime.before(endTime));
    }

    /**
     * using a thread to test performance, for simpleDateFormat is not thread-safe.
     */
    @Benchmark
    public void benchmarkDateFormat() {
        synchronized (BenchmarkTimeFormatTest.class) {
            str(simpleDateFormat.format(startTime));
        }
    }

    @Benchmark
    public void benchmarkDateFormatThreadSafe() {
        str(new SimpleDateFormat(DATE_TIME_FORMAT).format(startTime));
    }

    @Benchmark
    public void benchmarkDateParse() throws ParseException {
        synchronized (BenchmarkTimeFormatTest.class) {
            date(simpleDateFormat.parse("2020-12-12 11:22:33"));
        }
    }

    @Benchmark
    public void benchmarkDateParseThreadSafe() throws ParseException {
        date(new SimpleDateFormat(DATE_TIME_FORMAT).parse("2020-12-12 11:22:33"));
    }

    @Benchmark
    public void benchmarkLocalDateTimeCompare() {
        bool(startDateTime.isBefore(endDateTime));
    }

    @Benchmark
    public void benchmarkLocalDateTimeFormat() {
        str(startDateTime.format(timeFormatter));
    }

    @Benchmark
    public void benchmarkLocalDateTimeParse() throws ParseException {
        dateTime(LocalDateTime.parse("2020-12-12 11:22:33", timeFormatter));
    }

}
