package com.observability.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ComputeService {

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

    /** add jittery latency 20–200 ms */
    public void jitterSleep() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(20, 201));
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}

