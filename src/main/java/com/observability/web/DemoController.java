package com.observability.web;

import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.annotation.Observed;
import com.observability.dto.ErrorResponse;
import com.observability.dto.ComputeResponse;
import com.observability.dto.HelloResponse;
import com.observability.service.ComputeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.Instant;

@RestController
@RequestMapping("/api")
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);
    private final ComputeService compute;

    public DemoController(ComputeService compute) { this.compute = compute; }

    @GetMapping("/hello")
    @Observed(name = "demo.hello", contextualName = "hello")
    @Timed(value = "demo.hello.latency")
    public ResponseEntity<HelloResponse> hello(@RequestParam(defaultValue = "world") String name) {
        var ts = Instant.now().toEpochMilli();
        var body = new HelloResponse("Hello, " + name + "!", "/api/hello", ts);
        log.info("hello request name={}", name);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/compute")
    @Observed(name = "demo.compute", contextualName = "compute")
    @Timed(value = "demo.compute.latency")
    public ResponseEntity<ComputeResponse> compute(@RequestParam(defaultValue = "10") int n) {
        var start = System.currentTimeMillis();
        compute.maybeFail();       // may throw -> handled below
        int result = compute.fib(n);
        compute.jitterSleep();

        int latency = (int) (System.currentTimeMillis() - start);
        var ts = System.currentTimeMillis();
        var body = new ComputeResponse(result, "/api/compute", ts, latency);
        log.info("compute n={} result={} latency_ms={}", n, result, latency);
        return ResponseEntity.ok(body);
    }
}

