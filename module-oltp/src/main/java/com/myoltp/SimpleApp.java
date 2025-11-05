package com.myoltp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.util.UUID;

public class SimpleApp {
    private static final Logger log = LoggerFactory.getLogger(SimpleApp.class);

    public static void main(String[] args) {
        MDC.put("requestId", UUID.randomUUID().toString());  // <-- set BEFORE logging
        log.info("App starting…");
        int n = 5;
        log.info("computeSquare start n={}", n);
        int result = computeSquare(n);
        log.info("computeSquare result={}", result);
        MDC.clear(); // avoid leakage
    }

    private static int computeSquare(int n) {
        try { Thread.sleep(25); } catch (InterruptedException ignored) {}
        return n * n;
    }
}
