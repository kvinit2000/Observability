package com.myoltp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.util.UUID;

public class SimpleApp {
    private static final Logger log = LoggerFactory.getLogger(SimpleApp.class);
    public static void main(String[] args) {
        MDC.put("requestId", UUID.randomUUID().toString());
        log.info("App starting...");

        int result = computeSquare(5);

        log.info("Result = " + result);
        MDC.clear();
    }

    // one method
    private static int computeSquare(int n) {
        return n * n;
    }
}
