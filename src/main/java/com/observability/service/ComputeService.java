package com.observability.service;

import com.observability.dto.ComputeResponse;
import com.observability.dto.HelloResponse;
import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ComputeService {

    @Observed(name = "demo.hello")
    @Timed(value = "demo.hello.latency")
    public HelloResponse computeHello(String name) {
        long ts = Instant.now().toEpochMilli();
        return new HelloResponse("Hello, " + name + "!", "/api/hello", ts);
    }

    @Observed(name = "demo.compute")
    @Timed(value = "demo.compute.latency")
    public ComputeResponse computeFibTimed(int n) {
        long start = System.currentTimeMillis();

        maybeFail();
        int result = fib(n);
        jitterSleep();

        int latency = (int) (System.currentTimeMillis() - start);

        return new ComputeResponse(result, "/api/compute",
                System.currentTimeMillis(), latency);
    }

    /** fibonacci — same logic as before */
    public int fib(int n) {
        n = Math.max(1, Math.min(30, n));   // clamp 1–30
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            int tmp = a + b;
            a = b;
            b = tmp;
        }
        return a; // F(n)
    }

    /** ~5% simulated failure */
    public void maybeFail() {
        if (ThreadLocalRandom.current().nextDouble() < 0.05) {
            throw new RuntimeException("simulated_failure");
        }
    }

    /** jitter 20–200ms */
    public void jitterSleep() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(20, 201));
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
