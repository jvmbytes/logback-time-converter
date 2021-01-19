package com.jvmbytes.logback.converter;

import ch.qos.logback.classic.pattern.DateConverter;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.text.ParseException;

@State(Scope.Benchmark)
public class BenchmarkDateTimeConverterTest {

    // Benchmark                                                   Mode  Cnt          Score          Error  Units
    // BenchmarkDateTimeConverterTest.benchmarkDateConverter      thrpt    5    7975504.094 ±  1433586.972  ops/s
    // BenchmarkDateTimeConverterTest.benchmarkDateTimeConverter  thrpt    5  180500615.776 ± 64785117.436  ops/s
    public static void main(String[] args) throws Exception {
        Options options =
            new OptionsBuilder().include(BenchmarkDateTimeConverterTest.class.getSimpleName()).forks(1).threads(64)
                .warmupIterations(1).build();
        new Runner(options).run();
    }

    @Benchmark
    public void benchmarkDateConverter() {
        LoggingEvent le = new LoggingEvent();
        le.setTimeStamp(System.currentTimeMillis());
        dateConverter.convert(le);
    }

    @Benchmark
    public void benchmarkDateTimeConverter() {
        LoggingEvent le = new LoggingEvent();
        le.setTimeStamp(System.currentTimeMillis());
        dateTimeConverter.convert(le);
    }

    DateConverter dateConverter;
    DateTimeConverter dateTimeConverter;

    @Setup
    public void initTest() throws ParseException {
        dateConverter = new DateConverter();
        dateConverter.start();
        dateTimeConverter = new DateTimeConverter();
        dateTimeConverter.start();
    }

}
