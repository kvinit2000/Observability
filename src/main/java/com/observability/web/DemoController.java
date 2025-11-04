package com.observability.web;

import com.observability.dto.ComputeResponse;
import com.observability.dto.HelloResponse;
import com.observability.service.ComputeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);
    private final ComputeService compute;

    public DemoController(ComputeService compute) { this.compute = compute; }

    @GetMapping("/hello")
    public ResponseEntity<HelloResponse> hello(@RequestParam(defaultValue = "world") String name) {
        var body = compute.computeHello(name);
        log.info("hello request name={}", name);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/compute")
    public ResponseEntity<ComputeResponse> compute(@RequestParam(defaultValue = "10") int n) {
        var body = compute.computeFibTimed(n);
        log.info("compute n={} result={} latency_ms={}", n, body.result(), body.latency_ms());
        return ResponseEntity.ok(body);
    }
}
