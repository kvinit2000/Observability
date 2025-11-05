package com.myoltp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.ThreadContext;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

import io.opentelemetry.exporter.logging.LoggingSpanExporter;

import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

import io.opentelemetry.semconv.ResourceAttributes;

// *** THIS IS THE MISSING IMPORT BLOCK ***
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;

public class SimpleApp {
    private static final Logger log = LoggerFactory.getLogger(SimpleApp.class);

    public static void main(String[] args) {
        log.info("App starting…");

        Resource resource = Resource.getDefault()
                .merge(Resource.create(
                        Attributes.of(ResourceAttributes.SERVICE_NAME, "module-oltp")
                ));

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
                .setResource(resource)
                .build();

        // *** THE ONE MISSING LINE WAS THIS setPropagators(...) ***
        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();

        Tracer tracer = openTelemetry.getTracer("module-oltp");

        Span span = tracer.spanBuilder("computeSquare").startSpan();
        log.info("span valid? {}", span.getSpanContext().isValid());

        long t0 = System.nanoTime();

        try (Scope ignored = span.makeCurrent()) {

            // put IDs into log4j2 ThreadContext (this makes %X{} work)
            ThreadContext.put("trace_id", span.getSpanContext().getTraceId());
            ThreadContext.put("span_id",  span.getSpanContext().getSpanId());

            int n = 5;
            log.info("computeSquare start n={}", n);

            int result = computeSquare(n);

            double ms = (System.nanoTime() - t0) / 1_000_000.0;
            log.info("computeSquare result={} latencyMs={}", result, String.format("%.3f", ms));

        } finally {
            ThreadContext.clearAll();
            span.end();
        }

        tracerProvider.close();
        log.info("App finished");
    }

    private static int computeSquare(int n) {
        try { Thread.sleep(25); } catch (InterruptedException ignored) {}
        return n * n;
    }
}
