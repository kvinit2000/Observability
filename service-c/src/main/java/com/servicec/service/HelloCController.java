package com.servicec.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloCController {

    @GetMapping("/api/c/compute")
    public Result compute(@RequestParam int n) {
        long r = 1;
        for (int i = 2; i <= n; i++) r *= i;   // toy work
        return new Result(n, r);
    }

    record Result(int n, long result) {}
}

