package com.myoltp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleApp {
    private static final Logger log = LoggerFactory.getLogger(SimpleApp.class);
    public static void main(String[] args) {
        log.debug("App starting...");

        int result = computeSquare(5);

        log.debug("Result = " + result);
    }

    // one method
    private static int computeSquare(int n) {
        return n * n;
    }
}
