package com.myoltp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

public class SimpleApp {
    private static final Logger log = LoggerFactory.getLogger(SimpleApp.class);

    public static void main(String[] args) {
        log.info("App starting…");

        // fetch tracer AFTER JVM + agent are fully started
        Tracer tracer = GlobalOpenTelemetry.getTracer("module-oltp");

        Span span = tracer.spanBuilder("computeSquare").startSpan();
        boolean valid = span.getSpanContext().isValid();
        log.info("span valid? {}", valid);

        long t0 = System.nanoTime();
        try (Scope ignored = span.makeCurrent()) {
            MDC.put("trace_id", span.getSpanContext().getTraceId());
            MDC.put("span_id",  span.getSpanContext().getSpanId());

            int n = 5;
            log.info("computeSquare start n={}", n);
            int result = computeSquare(n);
            double ms = (System.nanoTime() - t0) / 1_000_000.0;
            log.info("computeSquare result={} latencyMs={}", result, String.format("%.3f", ms));
        } finally {
            MDC.clear();
            span.end();
        }
    }

    private static int computeSquare(int n) {
        try { Thread.sleep(25); } catch (InterruptedException ignored) {}
        return n * n;
    }
}
