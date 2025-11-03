package com.observability.dto;

public record ComputeResponse(int result, String endpoint, long ts, int latency_ms) {}

