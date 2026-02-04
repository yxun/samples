package com.example;

import java.io.IOException;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;

public class ManualJvmMetricsExample {

    public static void main(String[] args) throws InterruptedException, IOException {
        JvmMetrics.builder().register();

        HTTPServer server = HTTPServer.builder()
        .port(8084)
        .buildAndStart();

        System.out.println("HTTPServer listening on http://localhost:" + server.getPort() + "/metrics");

        Thread.currentThread().join();
    }
}
